package org.example;

import java.util.*;
import java.util.stream.Collectors;

class GeneEmbedding {
    public String gene;
    public double[] vector;

    public GeneEmbedding(String gene, double[] vector) {
        this.gene = gene;
        this.vector = vector;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gene + ": [");
        for (int i = 0; i < vector.length; i++) {
            sb.append(String.format("%.4f", vector[i]));
            if (i < vector.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
