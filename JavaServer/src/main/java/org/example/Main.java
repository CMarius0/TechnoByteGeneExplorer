package org.example;

import java.net.MalformedURLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(APICalls.getGeneIdFromSymbol("TP53"));
        System.out.println(APICalls.getGeneInfoFromID(APICalls.getGeneIdFromSymbol("TP53")));

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
    }
}