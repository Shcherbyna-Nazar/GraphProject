package pw.edu.pl.jimppro.algorithms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pw.edu.pl.jimppro.graph.Graph;
import pw.edu.pl.jimppro.graph.Vertex;
import pw.edu.pl.jimppro.mainpack.Launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DijkstraAlgorithmTest {
    static Graph graph;
    static DijkstraAlgorithm dijkstra;

    @BeforeAll
    public static void setUp() {
        Launcher.start();
        graph = new Graph(2, 3);
        graph.addEdge(new Vertex(0), new Vertex(2), 2);
        graph.addEdge(new Vertex(2), new Vertex(4), 4);
        graph.addEdge(new Vertex(2), new Vertex(5), 1);
        graph.addEdge(new Vertex(0), new Vertex(3), 1);
        graph.addEdge(new Vertex(3), new Vertex(4), 1);
        graph.addEdge(new Vertex(4), new Vertex(3), 4);
        graph.addEdge(new Vertex(0), new Vertex(1), 3);
        dijkstra = new DijkstraAlgorithm(graph);
    }

    @Test
    void shouldEqualsShortestPathLength() {
        dijkstra.run(new Vertex(0));
        double expectedLengthTo0 = 0;
        double expectedLengthTo1 = 3;
        double expectedLengthTo2 = 2;
        double expectedLengthTo3 = 1;
        double expectedLengthTo4 = 2;
        double expectedLengthTo5 = 3;
        Double[] length = dijkstra.getLength();
        assertEquals(expectedLengthTo0, length[0]);
        assertEquals(expectedLengthTo1, length[1]);
        assertEquals(expectedLengthTo2, length[2]);
        assertEquals(expectedLengthTo3, length[3]);
        assertEquals(expectedLengthTo4, length[4]);
        assertEquals(expectedLengthTo5, length[5]);
    }

    @Test
    void shouldEqualsParentOfVertex() {
        dijkstra.run(new Vertex(0));
        Vertex expected1 = new Vertex(0);
        Vertex expected2 = new Vertex(0);
        Vertex expected3 = new Vertex(0);
        Vertex expected4 = new Vertex(3);
        Vertex expected5 = new Vertex(2);
        Vertex[] parent = dijkstra.getParent();
        assertEquals(expected1, parent[1]);
        assertEquals(expected2, parent[2]);
        assertEquals(expected3, parent[3]);
        assertEquals(expected4, parent[4]);
        assertEquals(expected5, parent[5]);
    }

}