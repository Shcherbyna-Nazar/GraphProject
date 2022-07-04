package pw.edu.pl.jimppro.graph;

import java.util.Objects;

public class Edge {
    private final Vertex destination;
    private final double weight;

    public Edge(Vertex destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return Double.compare(edge.weight, weight) == 0 && Objects.equals(destination, edge.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, weight);
    }

    public double getWeight() {
        return weight;
    }

    public Vertex getDestination() {
        return destination;
    }
}
