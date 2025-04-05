package org.example;


public class Main {
    public static void main(String[] args) {
        System.out.println(APICalls.getGeneInfoFromID(APICalls.getGeneIdFromSymbol("TP53")));
        System.out.println(APICalls.getPathwaysFromID(APICalls.getGeneIdFromSymbol("TP53")));
    }
}