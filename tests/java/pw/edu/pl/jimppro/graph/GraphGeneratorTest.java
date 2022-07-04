package pw.edu.pl.jimppro.graph;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pw.edu.pl.jimppro.mainpack.Launcher;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphGeneratorTest {

    static Graph graph;
    static GraphGenerator graphGenerator;

    @BeforeAll
    public static void setUp() {
        Launcher.start();
    }

    @BeforeEach
    public void beforeEach() {
        graphGenerator = new GraphGenerator(3, 4);
    }

    @Test
    void shouldBeTrueGenerateMode1Connected() throws Exception {

        graph = graphGenerator.generate(1, 0, 1);
        boolean result = graph.directedToUndirected().isConnected(new Vertex(0));

        assertTrue(result);
    }

    @Test
    void shouldBeTrueGenerateMode2Connected() {
        graph = graphGenerator.generate(2, 0, 1);
        boolean result = graph.directedToUndirected().isConnected(new Vertex(0));

        assertTrue(result);
    }

    @Test
    void shouldEqualsGenerateMode1CorrectSize() {
        graph = graphGenerator.generate(1, 0, 1);
        LinkedList[] adj = graph.getAdj();
        for (int i = 0; i < graph.getSize(); i++) {
            assertEquals(graph.countEdges(i), adj[i].size());
        }
    }

    @Test
    void shouldBeTrueGenerateMode2CorrectSize() {
        graph = graphGenerator.generate(2, 0, 1);
        LinkedList<Edge>[] adj = graph.getAdj();
        for (int i = 0; i < graph.getSize(); i++) {
            if (graph.countEdges(i) < adj[i].size()) {
                assertTrue(false);
            }
        }
        assertTrue(true);
    }

    @Test
    void shouldBeTrueGenerateCorrectWeights() {
        double lower_limit = 0, upper_limit = 1;
        graph = graphGenerator.generate(1, lower_limit, upper_limit);
        LinkedList<Edge>[] adj = graph.getAdj();
        for (int i = 0; i < graph.getSize(); i++) {
            for (Edge edge : adj[i]) {
                if (edge.getWeight() < lower_limit || edge.getWeight() >= upper_limit) {
                    assertTrue(false);
                }
            }
        }
        assertTrue(true);
    }

}