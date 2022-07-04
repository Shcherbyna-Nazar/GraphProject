package pw.edu.pl.jimppro.graph;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pw.edu.pl.jimppro.mainpack.Launcher;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    static Graph graph;

    @BeforeAll
    public static void setUp() {
        Launcher.start();
    }

    @BeforeEach
    public void beforeEach() {
        graph = new Graph(3, 3);
        graph.addEdge(new Vertex(0), new Vertex(1), 2);
        graph.addEdge(new Vertex(0), new Vertex(3), 3);
        graph.addEdge(new Vertex(1), new Vertex(0), 1);
        graph.addEdge(new Vertex(1), new Vertex(2), 1);
        graph.addEdge(new Vertex(1), new Vertex(4), 1);
        graph.addEdge(new Vertex(2), new Vertex(1), 4);
        graph.addEdge(new Vertex(2), new Vertex(5), 4);
        graph.addEdge(new Vertex(3), new Vertex(0), 3);
        graph.addEdge(new Vertex(3), new Vertex(4), 2);
        graph.addEdge(new Vertex(3), new Vertex(6), 5);
        graph.addEdge(new Vertex(4), new Vertex(1), 1);
        graph.addEdge(new Vertex(4), new Vertex(3), 4);
        graph.addEdge(new Vertex(4), new Vertex(5), 2);
        graph.addEdge(new Vertex(4), new Vertex(7), 2);
        graph.addEdge(new Vertex(5), new Vertex(2), 1);
        graph.addEdge(new Vertex(5), new Vertex(4), 4);
        graph.addEdge(new Vertex(5), new Vertex(8), 2);
        graph.addEdge(new Vertex(6), new Vertex(3), 3);
        graph.addEdge(new Vertex(6), new Vertex(7), 1);
        graph.addEdge(new Vertex(7), new Vertex(4), 1);
        graph.addEdge(new Vertex(7), new Vertex(6), 2);
        graph.addEdge(new Vertex(7), new Vertex(8), 6);
        graph.addEdge(new Vertex(8), new Vertex(5), 5);
        graph.addEdge(new Vertex(8), new Vertex(7), 1);
    }

    @Test
    void shouldEqualsFindPath() {
        LinkedList<Vertex> expectedFrom0To5 = new LinkedList<>();
        expectedFrom0To5.add(new Vertex(0));
        expectedFrom0To5.add(new Vertex(1));
        expectedFrom0To5.add(new Vertex(4));
        expectedFrom0To5.add(new Vertex(5));
        LinkedList<Vertex> expectedFrom2To8 = new LinkedList<>();
        expectedFrom2To8.add(new Vertex(2));
        expectedFrom2To8.add(new Vertex(5));
        expectedFrom2To8.add(new Vertex(8));

        LinkedList<Vertex> actualFrom0To5 = graph.findPath(new Vertex(0), new Vertex(5));
        LinkedList<Vertex> actualFrom2To8 = graph.findPath(new Vertex(2), new Vertex(8));

        assertEquals(expectedFrom0To5, actualFrom0To5);
        assertEquals(expectedFrom2To8, actualFrom2To8);
    }

    @Test
    void shouldNotEqualsFindPath() {
        graph.clear();
        LinkedList<Vertex> expected = new LinkedList<>();
        expected.add(new Vertex(0));
        expected.add(new Vertex(1));
        expected.add(new Vertex(4));
        expected.add(new Vertex(5));
        LinkedList<Vertex> actual = graph.findPath(new Vertex(0), new Vertex(5));
        assertNotEquals(expected, actual);
    }

    @Test
    void shouldBeTrueIsConnected() {
        assertTrue(graph.isConnected(new Vertex(0)));
    }

    @Test
    void shouldBeFalseIsConnected() {
        graph.clear();
        assertFalse(graph.isConnected(new Vertex(0)));
    }

    @Test
    void shouldTrueSaveToFileCorrectly() {
        graph.saveToFile("testFile");
        Graph testGraph = new Graph();
        try {
            testGraph.read("testFile");
        } catch (IOException e) {
            fail("I/O exception occurred.");
        }
        if (testGraph.getSize() != graph.getSize())
            fail("Incorrect saving to file");

        LinkedList<Edge>[] adj = graph.getAdj();
        LinkedList<Edge>[] testAdj = testGraph.getAdj();
        for (int i = 0; i < graph.getSize(); i++) {
            if (testAdj[i].size() != adj[i].size()) {
                fail("Incorrect saving to file");
            }
            for (int j = 0; j < testAdj[i].size(); j++) {
                Edge expectedEdge = adj[i].get(j);
                Edge testEdge = testAdj[i].get(j);
                assertEquals(expectedEdge, testEdge);
            }
        }

    }
}