package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class GeneEmbeddingAPI {
    private final InteractionGraphBuilder graphBuilder = new InteractionGraphBuilder();
    private final EmbeddingService embeddingService = new EmbeddingService();
    private final Map<String, GeneEmbedding> embeddingMap = new HashMap<>();

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
}