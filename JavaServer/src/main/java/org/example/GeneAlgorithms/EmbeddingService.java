package org.example.GeneAlgorithms;

import org.apache.commons.math3.linear.*;
import java.util.*;

class EmbeddingService {

    public List<GeneEmbedding> computeEmbeddings(double[][] matrix, List<String> orderedGenes, int dimensions) {
        RealMatrix mat = MatrixUtils.createRealMatrix(matrix);
        SingularValueDecomposition svd = new SingularValueDecomposition(mat);

        RealMatrix U = svd.getU();
        double[] singularValues = svd.getSingularValues();

        int k = Math.min(dimensions, singularValues.length);
        RealMatrix U_k = U.getSubMatrix(0, U.getRowDimension() - 1, 0, k - 1);

        double[][] sigma_k = new double[k][k];
        for (int i = 0; i < k; i++) {
            sigma_k[i][i] = singularValues[i];
        }
        RealMatrix Sigma_k = MatrixUtils.createRealMatrix(sigma_k);

        RealMatrix embeddingMatrix = U_k.multiply(Sigma_k);

        List<GeneEmbedding> result = new ArrayList<>();
        for (int i = 0; i < orderedGenes.size(); i++) {
            double[] vector = embeddingMatrix.getRow(i);
            result.add(new GeneEmbedding(orderedGenes.get(i), vector));
        }
        return result;
    }

    public double computeCosineSimilarity(GeneEmbedding a, GeneEmbedding b) {
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < a.vector.length; i++) {
            dot += a.vector[i] * b.vector[i];
            normA += a.vector[i] * a.vector[i];
            normB += b.vector[i] * b.vector[i];
        }
        if (normA == 0 || normB == 0) return 0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
