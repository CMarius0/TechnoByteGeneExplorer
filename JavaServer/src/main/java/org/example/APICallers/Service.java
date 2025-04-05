package org.example.APICallers;

import org.example.GeneEmbeddingAPI;
import org.example.GeneInteraction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
        try {
            ArrayList<JSONObject> gene = new ArrayList<>();
            for(String geneName : geneNames){
                JSONObject obj = new JSONObject();
                JSONArray array = new JSONArray();
                try {
                    List<String> lista = keggAPICaller.extractDrugTarget(geneName.replaceAll("hsa0", ""));
                    if (lista!=null) {
                        for (var x : lista)
                            array.put(x);
                        obj.put("Drugs", array);
                        gene.add(obj);
                    }
                }
                catch (FileNotFoundException _){}
            }
            return gene;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray getDrugsForSimilarGenes(String geneName) {
        try {
            ArrayList<String> names = new ArrayList<>();;
            ArrayList<String> pathwayIDs = keggAPICaller.getPathwaysFromID(ncbiAPICaller.getGeneIdFromSymbol(geneName));
            for(var pathway : pathwayIDs){
                List<GeneInteraction> interactions = geneEmbeddingAPI.buildKeggBasedInteractions(pathway);
                geneEmbeddingAPI.loadInteractions(interactions,10);
                List<String> genes = keggAPICaller.extractGenesFromPathway(pathway);
                for (var gene : genes) {
                    JSONObject obj = new JSONObject(ncbiAPICaller.getGeneInfoFromID(Integer.parseInt(gene.replace("hsa0",""))).toString());
                    if(geneEmbeddingAPI.similarity(geneName,obj.getString("name")) > 0.5)
                        names.add(gene);
                }
            }
            JSONArray array = new JSONArray();
            List<JSONObject> drugs = getAssociatedDrugs(names);
            for(JSONObject obj : drugs){
                array.put(obj);
            }
            return array;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
