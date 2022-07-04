package pw.edu.pl.jimppro.algorithms;

import pw.edu.pl.jimppro.graph.Edge;
import pw.edu.pl.jimppro.graph.Graph;
import pw.edu.pl.jimppro.graph.Vertex;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {
    private final Vertex[] parent;
    private final Double[] length;
    private final Graph graph;

    public DijkstraAlgorithm(Graph graph) {
        this.graph = graph;
        this.parent = new Vertex[graph.getSize()];
        this.length = new Double[graph.getSize()];
        for (int i = 0; i < graph.getSize(); i++) {
            length[i] = Double.MAX_VALUE;
        }
    }

    private static class VertexWithLength {
        Double length;
        Vertex v;

        public VertexWithLength(Double length, Vertex v) {
            this.length = length;
            this.v = v;
        }
    }

    public void run(Vertex start) {
        for (int i = 0; i < graph.getSize(); i++) {
            length[i] = Double.MAX_VALUE;
            parent[i] = new Vertex(i);
        }
        PriorityQueue<VertexWithLength> queue = new PriorityQueue<>(graph.getSize(), new Comparator<>() {
            @Override
            public int compare(VertexWithLength v1, VertexWithLength v2) {
                return v1.length.compareTo(v2.length);

            }
        });
        length[start.getNumber()] = 0.0;
        queue.add(new VertexWithLength(0.0, start));
        while (!queue.isEmpty()) {
            VertexWithLength vl = queue.poll();
            Vertex cur_v = vl.v;
            Double cur_len = vl.length;
            if (cur_len > length[cur_v.getNumber()]) {
                continue;
            }
            LinkedList<Edge>[] adj = graph.getAdj();
            for (Edge edge : adj[cur_v.getNumber()]) {
                Vertex to = edge.getDestination();
                Double len = edge.getWeight();
                if (length[cur_v.getNumber()] + len < length[to.getNumber()]) {
                    length[to.getNumber()] = length[cur_v.getNumber()] + len;
                    parent[to.getNumber()] = cur_v;
                    queue.add(new VertexWithLength(length[to.getNumber()], to));
                }
            }
        }

    }

    public Vertex[] getParent() {
        return parent;
    }

    public Double[] getLength() {
        return length;
    }


}
