package org.example.GeneAlgorithms;

import java.util.Objects;

// --- Data Model: GeneInteraction ---
public class GeneInteraction {
    public String sourceGene;
    public String targetGene;
    public String relationType; // "activates" or "inhibits"

    public GeneInteraction(String sourceGene, String targetGene, String relationType) {
        this.sourceGene = sourceGene;
        this.targetGene = targetGene;
        this.relationType = relationType.toLowerCase();
    }

    @Override
    public String toString() {
        return sourceGene + " -[" + relationType + "]-> " + targetGene;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneInteraction that = (GeneInteraction) o;
        return sourceGene.equals(that.sourceGene) &&
                targetGene.equals(that.targetGene) &&
                relationType.equals(that.relationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceGene, targetGene, relationType);
    }
}
