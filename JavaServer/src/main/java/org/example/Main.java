package org.example;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.util.List;

import static org.example.APICalls.getDiseasesByGeneId;


public class Main {
    public static void main(String[] args) {
        System.out.println(APICalls.getGeneInfoFromID(APICalls.getGeneIdFromSymbol("TP53")));
        System.out.println(APICalls.getPathwaysFromID(APICalls.getGeneIdFromSymbol("TP53")));
        /*
        List<String> lista = APICalls.request("get/hsa:7293");
        List<String> lista2 = APICalls.request("list/hsa");
        if (lista != null)
            for (var x: lista)
                System.out.println(x);
        if (lista2 != null)
            for (var x: lista2)
                System.out.println(x);
         */

        /*
        try {
            List<String> diseases = getDiseasesByGeneId("7157"); // TP53
            System.out.println("Diseases linked to gene 7157 (TP53):");
            diseases.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } */
    }
}