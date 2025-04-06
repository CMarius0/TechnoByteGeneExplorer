package org.example;

import org.example.APICallers.KeggAPICaller;
import org.example.APICallers.NcbiAPICaller;

public class Main {
    public static void main(String[] args) {
        Service service = new Service();
        KeggAPICaller keggAPICaller = new KeggAPICaller("https://rest.kegg.jp/");
        NcbiAPICaller apiCaller = new NcbiAPICaller("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/");
        try {
            System.out.println(service.getDrugsForSimilarGenes("TP53"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}