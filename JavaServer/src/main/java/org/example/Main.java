package org.example;

<<<<<<< Updated upstream


public class Main {
    public static void main(String[] args) {
=======
import org.json.JSONArray;

import java.net.MalformedURLException;
import java.util.List;

import static org.example.APICalls.*;


public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(APICalls.getGeneInfoFromID(APICalls.getGeneIdFromSymbol("TP53")));
        System.out.println(APICalls.getPathwaysFromID(APICalls.getGeneIdFromSymbol("TP53")));
        /*
        List<String> lista = APICalls.request("get/hsa04060");
        if (lista != null)
            for (var x: lista)
                System.out.println(x);
        List<String> lista2 = APICalls.request("list/hsa");

        if (lista != null)
            for (var x: lista)
                System.out.println(x);
        if (lista2 != null)
            for (var x: lista2)
                System.out.println(x);
         */

        /*
>>>>>>> Stashed changes
        try {
            System.out.println(APICalls.getDiseasesByGeneId(String.valueOf(APICalls.getGeneIdFromSymbol("TP53"))));
        } catch (Exception e) {
<<<<<<< Updated upstream
            throw new RuntimeException(e);
        }
=======
            e.printStackTrace();
        }*/

        //List<String> genes = extractGenesFromPathway("path:hsa05200");
        //System.out.println("Genes in pathway hsa05200:");
        //genes.forEach(System.out::println);
        //String dna = extractNtseqFromKeggGene("hsa:7157");
        //System.out.println("DNA fragment: " + dna);
>>>>>>> Stashed changes
    }
}