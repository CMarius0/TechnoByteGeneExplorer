package org.example.APICallers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Service {
    NcbiAPICaller ncbiAPICaller = new NcbiAPICaller("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/");
    KeggAPICaller keggAPICaller = new KeggAPICaller("https://rest.kegg.jp/");

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
                catch (FileNotFoundException e){
                    continue;
                }
            }
            return gene;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
