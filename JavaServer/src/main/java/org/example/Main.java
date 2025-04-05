package org.example;



public class Main {
    public static void main(String[] args) {
        try {
            System.out.println(APICalls.getDiseasesByGeneId(String.valueOf(APICalls.getGeneIdFromSymbol("TP53"))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}