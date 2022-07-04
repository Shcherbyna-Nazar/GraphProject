package pw.edu.pl.jimppro.graph;

import java.util.Random;

public class GraphGenerator {
    Graph graph;
    Random random;
    double probability = 0;

    public GraphGenerator(int rows, int columns) {
        this.graph = new Graph(rows, columns);
        this.random = new Random();
        if (rows * columns < 3 && rows * columns > 0) {
            this.probability = 0.5;
        } else if (rows * columns >= 3) {
            this.probability = 1 - 1 / Math.log(rows * columns);
        }
    }

    public Graph generate(int mode, double lower_limit, double upper_limit) {
        if (mode == 1) {
            for (int i = 0; i < graph.getRows(); i++) {
                for (int j = 0; j < graph.getColumns(); j++) {
                    double randomWeight = randomValue(lower_limit, upper_limit);
                    if (j != (graph.getColumns() - 1)) {
                        Vertex from = new Vertex(i * graph.getColumns() + j);
                        Vertex to = new Vertex(i * graph.getColumns() + j + 1);
                        graph.addEdge(from, to, randomWeight);
                        randomWeight = randomValue(lower_limit, upper_limit);
                        graph.addEdge(to, from, randomWeight);
                    }
                    randomWeight = randomValue(lower_limit, upper_limit);
                    if (i != (graph.getRows() - 1)) {
                        Vertex from = new Vertex(i * graph.getColumns() + j);
                        Vertex to = new Vertex((i + 1) * graph.getColumns() + j);
                        graph.addEdge(from, to, randomWeight);
                        randomWeight = randomValue(lower_limit, upper_limit);
                        graph.addEdge(to, from, randomWeight);
                    }
                }
            }
        } else if (mode == 3) {
            for (int i = 0; i < graph.getRows(); i++) {
                for (int j = 0; j < graph.getColumns(); j++) {
                    double randomWeight = randomValue(lower_limit, upper_limit);
                    if (j != (graph.getColumns() - 1)) {
                        Vertex from = new Vertex(i * graph.getColumns() + j);
                        Vertex to = new Vertex(i * graph.getColumns() + j + 1);
                        if (randomValue(0, 1) < probability) {
                            graph.addEdge(from, to, randomWeight);
                        }
                        randomWeight = randomValue(lower_limit, upper_limit);
                        if (randomValue(0, 1) < probability) {
                            graph.addEdge(to, from, randomWeight);
                        }
                    }
                    randomWeight = randomValue(lower_limit, upper_limit);
                    if (i != (graph.getRows() - 1)) {
                        Vertex from = new Vertex(i * graph.getColumns() + j);
                        Vertex to = new Vertex((i + 1) * graph.getColumns() + j);
                        if (randomValue(0, 1) < probability) {
                            graph.addEdge(from, to, randomWeight);
                        }
                        randomWeight = randomValue(lower_limit, upper_limit);
                        if (randomValue(0, 1) < probability) {
                            graph.addEdge(to, from, randomWeight);
                        }
                    }
                }
            }

        } else if (mode == 2) {
            Graph undirected;
            do {
                graph.clear();
                for (int i = 0; i < graph.getRows(); i++) {
                    for (int j = 0; j < graph.getColumns(); j++) {
                        double randomWeight = randomValue(lower_limit, upper_limit);
                        if (j != (graph.getColumns() - 1)) {
                            Vertex from = new Vertex(i * graph.getColumns() + j);
                            Vertex to = new Vertex(i * graph.getColumns() + j + 1);
                            if (randomValue(0, 1) < probability) {
                                graph.addEdge(from, to, randomWeight);
                            }
                            randomWeight = randomValue(lower_limit, upper_limit);
                            if (randomValue(0, 1) < probability) {
                                graph.addEdge(to, from, randomWeight);
                            }
                        }
                        randomWeight = randomValue(lower_limit, upper_limit);
                        if (i != (graph.getRows() - 1)) {
                            Vertex from = new Vertex(i * graph.getColumns() + j);
                            Vertex to = new Vertex((i + 1) * graph.getColumns() + j);
                            if (randomValue(0, 1) < probability) {
                                graph.addEdge(from, to, randomWeight);
                            }
                            randomWeight = randomValue(lower_limit, upper_limit);
                            if (randomValue(0, 1) < probability) {
                                graph.addEdge(to, from, randomWeight);
                            }
                        }
                    }
                }
                undirected = graph.directedToUndirected();

            } while (!undirected.isConnected(new Vertex(0)));
        }
        return graph;
    }

    private double randomValue(double lower_limit, double upper_limit) {
        Random random = new Random();
        return Math.round((lower_limit + random.nextDouble() * (upper_limit - lower_limit)) * 1000.0) / 1000.0;
    }
}
