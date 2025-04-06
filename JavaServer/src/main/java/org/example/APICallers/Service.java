package org.example.APICallers;

import org.example.GeneEmbeddingAPI;
import org.example.GeneInteraction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Service {
    NcbiAPICaller ncbiAPICaller = new NcbiAPICaller("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/");
    KeggAPICaller keggAPICaller = new KeggAPICaller("https://rest.kegg.jp/");
    GeneEmbeddingAPI geneEmbeddingAPI = new GeneEmbeddingAPI(keggAPICaller);

    public String getGene(String name) {
        JSONObject x;
        try {
            x = ncbiAPICaller.getGeneInfoFromID(ncbiAPICaller.getGeneIdFromSymbol(name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (x==null){
            x = new JSONObject();
            x.put("Error", "No matching Gene Name");
        }
        return x.toString();
    }

    public List<JSONObject> getAssociatedDrugs(ArrayList<String> geneNames) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<CompletableFuture<JSONObject>> futures = geneNames.stream()
                .map(geneName -> CompletableFuture.supplyAsync(() -> {
                    try {
                        Integer geneId = ncbiAPICaller.getGeneIdFromSymbol(geneName);
                        List<String> drugs = keggAPICaller.extractDrugTarget(geneId.toString());

                        if (drugs != null) {
                            JSONObject obj = new JSONObject();
                            JSONArray array = new JSONArray(drugs);
                            obj.put("Gene", geneName);
                            obj.put("Drugs", array);
                            return obj;
                        }
                    } catch (Exception _) {
                    }
                    return null;
                }, executor))
                .toList();

        List<JSONObject> result = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        executor.shutdown();
        return result;
    }

    public JSONArray getDrugsForSimilarGenes(String geneName) {
        try {
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> pathwayIDs = keggAPICaller.getPathwaysFromID(ncbiAPICaller.getGeneIdFromSymbol(geneName));
            int n = 5;
            for(var pathway : pathwayIDs){
                if (n==0)
                    break;
                List<GeneInteraction> interactions = geneEmbeddingAPI.buildKeggBasedInteractions(pathway,geneName);
                geneEmbeddingAPI.loadInteractions(interactions,20);
                List<String> genes = keggAPICaller.extractGenesFromPathway(pathway,geneName);
                for (var gene : genes) {
                    if(geneEmbeddingAPI.similarity(geneName,gene) > 0.5 && !names.contains(gene))
                        names.add(gene);
                }
                n--;
            }
            JSONArray array = new JSONArray();
            List<JSONObject> drugs = getAssociatedDrugs(names);
            for(JSONObject obj : drugs){
                int score = 0;
                if(names.contains(obj.getString("Gene")))
                    score+=10;
                ArrayList<String> pathways = keggAPICaller.getPathwaysFromID(ncbiAPICaller.getGeneIdFromSymbol(obj.getString("Gene")));
                for (var pathway: pathwayIDs)
                    if(pathways.contains(pathway)) {
                        score += 5;
                        break;
                    }
                obj.put("score", score);
                array.put(obj);
            }
            return array;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
