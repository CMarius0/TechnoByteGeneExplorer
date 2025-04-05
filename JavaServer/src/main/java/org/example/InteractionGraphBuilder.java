package org.example;

import java.util.*;

class InteractionGraphBuilder {
    private Map<String, Integer> geneIndexMap;
    private List<String> orderedGeneList;

    public double[][] buildNormalizedMatrix(List<GeneInteraction> interactions) {
        Set<String> genes = new HashSet<>();
        for (GeneInteraction gi : interactions) {
            genes.add(gi.sourceGene);
            genes.add(gi.targetGene);
        }

        orderedGeneList = new ArrayList<>(genes);
        Collections.sort(orderedGeneList);
        geneIndexMap = new HashMap<>();
        for (int i = 0; i < orderedGeneList.size(); i++) {
            geneIndexMap.put(orderedGeneList.get(i), i);
        }

        int n = orderedGeneList.size();
        double[][] matrix = new double[n][n];

        for (GeneInteraction gi : interactions) {
            int from = geneIndexMap.get(gi.sourceGene);
            int to = geneIndexMap.get(gi.targetGene);
            double value = gi.relationType.equals("activates") ? 1.0 : -1.0;
            matrix[from][to] = value;
        }

        double absSum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                absSum += Math.abs(matrix[i][j]);
            }
        }

        double normFactor = Math.sqrt(absSum);
        if (normFactor > 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] /= normFactor;
                }
            }
        }

        return matrix;
    }

    public List<String> getOrderedGeneList() {
        return orderedGeneList;
    }
}