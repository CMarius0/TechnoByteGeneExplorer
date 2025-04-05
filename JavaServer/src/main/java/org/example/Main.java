package org.example;

import org.example.APICallers.KeggAPICaller;
import org.example.APICallers.NcbiAPICaller;

import java.util.List;

import static org.example.GeneEmbeddingAPI.buildKeggBasedInteractions;

public class Main {
    public static void main(String[] args) {
<<<<<<< Updated upstream
        KeggAPICaller keggAPICaller = new KeggAPICaller("https://rest.kegg.jp/");
        NcbiAPICaller apiCaller = new NcbiAPICaller("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/");
        try {
            System.out.println(keggAPICaller.getPathwaysFromID(apiCaller.getGeneIdFromSymbol("TP53")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
=======
        String pathwayId = "hsa04115"; // p53 signaling pathway

        List<GeneInteraction> interactions = null;
        try {
            interactions = buildKeggBasedInteractions(pathwayId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        GeneEmbeddingAPI api = new GeneEmbeddingAPI();
        api.loadInteractions(interactions, 10);

        System.out.println("Genes with embeddings: " + api.getAvailableGenes());

        String g1 = "TP53";
        String g2 = "CDKN1A";
        System.out.println("Similarity(" + g1 + ", " + g2 + ") = " + api.similarity(g1, g2));
        System.out.println("Embedding for " + g1 + ": " + api.getEmbedding(g1));
>>>>>>> Stashed changes
    }
}