package pw.edu.pl.jimppro.algorithms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pw.edu.pl.jimppro.graph.Graph;
import pw.edu.pl.jimppro.graph.Vertex;
import pw.edu.pl.jimppro.mainpack.Launcher;

import static org.junit.jupiter.api.Assertions.*;

class BfsAlgorithmTest {

    static Graph graph;
    static BfsAlgorithm bfs;

    @BeforeAll
    public static void setUp() {
        Launcher.start();
        graph = new Graph(2, 3);
        graph.addEdge(new Vertex(0), new Vertex(2), 2);
        graph.addEdge(new Vertex(2), new Vertex(4), 3);
        graph.addEdge(new Vertex(2), new Vertex(5), 1);
        graph.addEdge(new Vertex(0), new Vertex(3), 1);
        graph.addEdge(new Vertex(3), new Vertex(4), 1);
        graph.addEdge(new Vertex(4), new Vertex(3), 4);
        graph.addEdge(new Vertex(0), new Vertex(1), 3);
        bfs = new BfsAlgorithm(graph);
    }

    @BeforeEach
    public void prepareToTest() {
        BfsAlgorithm bfs = new BfsAlgorithm(graph);
    }

    @Test
    void shouldBeTrueAllVisited() {
        bfs.run(new Vertex(0));
        boolean[] used = bfs.getUsed();
        for (int i = 0; i < graph.getSize(); i++) {
            if (!used[i]) {
                fail("Number of Vertex " + i + " must be visited");
            }
        }
        assertTrue(true);
    }

    @Test
    void shouldFalseAllVisitedVertex() {
        bfs.run(new Vertex(1));
        boolean[] used = bfs.getUsed();
        for (int i = 0; i < graph.getSize(); i++) {
            if (i != 1) {
                assertFalse(used[i]);
            } else assertTrue(used[i]);
        }
    }

    @Test
    void shouldEqualsTrueLengthToVertex() {
        bfs.run(new Vertex(0));
        Integer[] length = bfs.getLength();
        int expectedTo2 = 1;
        int expectedTo5 = 2;
        int expectedTo3 = 1;
        int expectedTo4 = 2;
        int expectedTo0 = 0;

        assertEquals(expectedTo2, length[2]);
        assertEquals(expectedTo5, length[5]);
        assertEquals(expectedTo4, length[4]);
        assertEquals(expectedTo3, length[3]);
        assertEquals(expectedTo0, length[0]);

    }


}