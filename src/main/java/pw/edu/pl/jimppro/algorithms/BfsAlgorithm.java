package pw.edu.pl.jimppro.algorithms;

import pw.edu.pl.jimppro.graph.Edge;
import pw.edu.pl.jimppro.graph.Graph;
import pw.edu.pl.jimppro.graph.Vertex;

import java.util.LinkedList;

public class BfsAlgorithm {
    private final boolean[] used;
    private final Graph graph;
    private final Vertex[] parent;
    private final Integer[] length;

    public BfsAlgorithm(Graph graph) {
        this.graph = graph;
        this.used = new boolean[graph.getSize()];
        this.parent = new Vertex[graph.getSize()];
        this.length = new Integer[graph.getSize()];
        for (int i = 0; i < graph.getSize(); i++) {
            length[i] = 0;
            used[i] = false;
        }
    }

    public void run(Vertex start) {
        for (int i = 0; i < graph.getSize(); i++) {
            used[i] = false;
            length[i] = 0;
        }
        if (graph.getSize() == 0) {
            return;
        }
        LinkedList<Vertex> queue = new LinkedList();
        used[start.getNumber()] = true;
        queue.add(start);
        while (queue.size() != 0) {
            Vertex v = queue.poll();
            LinkedList<Edge>[] adj = graph.getAdj();
            for (Edge edge : adj[v.getNumber()]) {
                Vertex to = edge.getDestination();
                if (!used[to.getNumber()]) {
                    queue.add(to);
                    int toNumber = to.getNumber();
                    used[toNumber] = true;
                    parent[toNumber] = v;
                    length[toNumber] = length[v.getNumber()] + 1;
                }
            }

        }

    }

    public boolean[] getUsed() {
        return used;
    }

    public Integer[] getLength() {
        return length;
    }


}