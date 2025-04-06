package org.example.GeneAlgorithms;

import org.example.APICallers.KeggAPICaller;

import java.util.*;


public class GeneEmbeddingAPI {
    private final InteractionGraphBuilder graphBuilder = new InteractionGraphBuilder();
    private final EmbeddingService embeddingService = new EmbeddingService();
    private final Map<String, GeneEmbedding> embeddingMap = new HashMap<>();
    private final KeggAPICaller geneCaller;

    public GeneEmbeddingAPI(KeggAPICaller geneCaller){
        this.geneCaller = geneCaller;
    }

    public void loadInteractions(List<GeneInteraction> interactions, int dimensions) {
        double[][] matrix = graphBuilder.buildNormalizedMatrix(interactions);
        List<String> orderedGenes = graphBuilder.getOrderedGeneList();
        List<GeneEmbedding> embeddings = embeddingService.computeEmbeddings(matrix, orderedGenes, dimensions);
        embeddingMap.clear();
        for (GeneEmbedding embedding : embeddings) {
            embeddingMap.put(embedding.gene, embedding);
        }
    }

    public double similarity(String geneA, String geneB) {
        GeneEmbedding a = embeddingMap.get(geneA);
        GeneEmbedding b = embeddingMap.get(geneB);
        if (a == null || b == null) return 0.0;
        return embeddingService.computeCosineSimilarity(a, b);
    }

    public GeneEmbedding getEmbedding(String gene) {
        return embeddingMap.get(gene);
    }

    public Set<String> getAvailableGenes() {
        return embeddingMap.keySet();
    }

    // --- Build GeneInteractions from KEGG Pathway using APICalls ---
    public List<GeneInteraction> buildKeggBasedInteractions(String pathwayId,String mainGene) throws Exception {
        List<String> rawGenes = geneCaller.extractGenesFromPathway(pathwayId,mainGene);

        // Normalize gene names: keep only the first alias (split by space, comma, slash, or hyphen)
        List<String> genes = new ArrayList<>();

        for (String gene : rawGenes) {
            genes.add(gene.strip());
        }

        List<GeneInteraction> interactions = new ArrayList<>();
        for (int i = 0; i < genes.size(); i++) {
            for (int j = 0; j < genes.size(); j++) {
                if (i != j) {
                    String source = genes.get(i);
                    String target = genes.get(j);
                    String type = (i < j) ? "activates" : "inhibits"; // simple causal rule
                    interactions.add(new GeneInteraction(source, target, type));
                }
            }
        }
        return interactions;
    }
}