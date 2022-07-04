package pw.edu.pl.jimppro.graph;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import pw.edu.pl.jimppro.algorithms.BfsAlgorithm;
import pw.edu.pl.jimppro.algorithms.DijkstraAlgorithm;
import pw.edu.pl.jimppro.gui.Logger;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

import static java.lang.Math.*;


public class Graph {
    private int rows;
    private int columns;
    private int size;
    private LinkedList[] adj;
    private Logger logger;
    private TextField bp;

    public Graph(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.size = rows * columns;
        adj = new LinkedList[size];
        for (int i = 0; i < size; i++) {
            adj[i] = new LinkedList<>();
        }
        this.logger = Logger.getInstance();
    }

    public Graph() {
        this.rows = 0;
        this.columns = 0;
        this.size = 0;
        this.logger = Logger.getInstance();
    }

    public void addEdge(Vertex from, Vertex to, double weight) {
        adj[from.getNumber()].add(new Edge(to, weight));

    }

    public boolean are_neighbours(Vertex first, Vertex second) {
        LinkedList<Edge> edges = adj[first.getNumber()];
        for (Edge edge : edges)
            if (edge.getDestination().equals(second)) {
                return true;
            }

        return false;
    }

    public void read(String filePath) throws IOException {

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            logger.setMessage("Can't find file: " + filePath, 0);
            return;
        }
        String[] dimension = br.readLine().split(" ");
        try {
            rows = Integer.parseInt(dimension[0]);
            columns = Integer.parseInt(dimension[1]);
            if (rows <= 0 || columns <= 0) {
                rows = 0;
                columns = 0;
                br.close();
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            logger.setMessage("Invalid format of file in line 1\n", 0);
            rows = 0;
            columns = 0;
            e.getStackTrace();
            br.close();
            return;
        }
        size = rows * columns;
        adj = new LinkedList[size];
        String line;
        int numOfVertex = 0;
        while ((line = br.readLine()) != null && numOfVertex < size) {
            String[] stringEdges = line.split(" ");
            if (stringEdges.length == 1 && stringEdges[0].length() == 0) {
                adj[numOfVertex] = new LinkedList();
                numOfVertex++;
                continue;
            }
            if (stringEdges.length % 2 != 0) {
                logger.setMessage("Invalid format of file in line: " + (numOfVertex + 2) + "\n", 0);
                size = 0;
                rows = 0;
                columns = 0;
                br.close();
                return;
            }
            adj[numOfVertex] = new LinkedList();
            for (int i = 0; i < stringEdges.length; i += 2) {
                try {
                    Vertex newNeighbour = new Vertex(Integer.parseInt(stringEdges[i]));
                    double weight = Double.parseDouble(stringEdges[i + 1].substring(1));
                    if (canBeNeighbours(new Vertex(numOfVertex), newNeighbour))
                        addEdge(new Vertex(numOfVertex), newNeighbour, weight);
                    else {
                        logger.setMessage(logger.getText() + "Vertex " + numOfVertex + " can't be a neighbour of vertex " + newNeighbour.getNumber() + ", so edge was ignored.\n", -1);
                    }

                } catch (NumberFormatException e) {
                    rows = 0;
                    columns = 0;
                    size = 0;
                    logger.setMessage("Invalid format of file in line: " + (numOfVertex + 2) + "\n", 0);
                    br.close();
                    return;
                }
            }
            numOfVertex++;
        }
        if (numOfVertex != size) {
            logger.setMessage("The size of graph is bigger than number of described edges!\n", 0);
            size = numOfVertex - 1;

        }
        br.close();
    }

    private boolean canBeNeighbours(Vertex vertex, Vertex newNeighbour) {
        if ((vertex.getNumber() == newNeighbour.getNumber() - 1) && ((vertex.getNumber() + 1) % columns != 0)) {
            return true;
        }
        if ((vertex.getNumber() == newNeighbour.getNumber() + 1) && ((vertex.getNumber() % columns != 0))) {
            return true;
        }
        if ((vertex.getNumber() == newNeighbour.getNumber() - columns) && (vertex.getNumber() < size - columns)) {
            return true;
        }
        if ((vertex.getNumber() == newNeighbour.getNumber() + columns) && (vertex.getNumber()) >= columns) {
            return true;
        }

        return false;
    }

    public void saveToFile(String filePath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(rows + " " + columns + '\n');
            for (int i = 0; i < getSize(); i++) {
                Iterator<Edge> it = adj[i].iterator();
                while (it.hasNext()) {
                    Edge edge = it.next();
                    bw.write(edge.getDestination().getNumber() + " :" + edge.getWeight() + " ");
                }
                bw.write('\n');
            }
            bw.close();
        } catch (FileNotFoundException e) {
            logger.setMessage("Incorrect path : '" + filePath + "' .", 0);
        } catch (IOException e) {
            logger.setMessage("I/O error\n", 0);
            e.printStackTrace();
        }


    }

    public LinkedList<Vertex> findPath(Vertex from, Vertex to) {
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(this);
        dijkstraAlgorithm.run(from);
        LinkedList<Vertex> path = new LinkedList<>();
        Vertex[] parent = dijkstraAlgorithm.getParent();
        Double[] length = dijkstraAlgorithm.getLength();
        if (length[to.getNumber()] == Double.MAX_VALUE) {
            logger.setMessage("Path from " + from.getNumber() + " to " + to.getNumber() + " doesn't exists.", -1);
            return null;
        }
        Vertex vertex = to;
        path.add(vertex);
        while (!vertex.equals(from)) {
            vertex = parent[vertex.getNumber()];
            path.addFirst(vertex);
        }
        return path;

    }

    public void display(Canvas canvas) {
        Affine affine = new Affine();
        double diameter = max((double) (180 / cbrt(getSize())), 25);
        canvas.setWidth(min(diameter * getColumns(), 4000));
        canvas.setHeight(min(diameter * getRows(), 4000));
        GraphicsContext grc = canvas.getGraphicsContext2D();
        grc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        affine.appendScale(diameter, diameter);
        grc.setTransform(affine);
        grc.setLineWidth(0.02);
        grc.setFont(new Font("10", 0.3));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (j != columns - 1) {
                    Vertex from = new Vertex(i * columns + j);
                    Vertex to = new Vertex(i * columns + j + 1);
                    if (are_neighbours(from, to) || are_neighbours(to, from)) {
                        grc.strokeLine(0.3 + j + 0.5, 0.3 + i + 0.25, 0.3 + j + 1, 0.3 + i + 0.25);
                        if (are_neighbours(from, to)) {
                            grc.fillOval(0.3 + j + 0.95, 0.3 + i + 0.2, 0.1, 0.1);
                        }
                        if (are_neighbours(to, from)) {
                            grc.fillOval(0.3 + j + 0.45, 0.3 + i + 0.2, 0.1, 0.1);
                        }
                    }
                }
                if (i != rows - 1) {
                    Vertex from = new Vertex(i * columns + j);
                    Vertex to = new Vertex((i + 1) * columns + j);
                    if (are_neighbours(from, to) || are_neighbours(to, from)) {
                        grc.strokeLine(0.3 + j + 0.25, 0.3 + i + 0.5, 0.3 + j + 0.25, 0.3 + i + 1);
                        if (are_neighbours(from, to)) {
                            grc.fillOval(0.3 + j + 0.2, 0.3 + i + 0.95, 0.1, 0.1);
                        }
                        if (are_neighbours(to, from)) {
                            grc.fillOval(0.3 + j + 0.2, 0.3 + i + 0.45, 0.1, 0.1);
                        }

                    }
                }
                grc.fillText(Integer.toString(i * columns + j), j + 0.19, i + 0.27);
                grc.fillOval(0.3 + j, 0.3 + i, 0.5, 0.5);
            }
        }


    }

    public boolean isConnected(Vertex start) {
        BfsAlgorithm bfs = new BfsAlgorithm(this);
        bfs.run(start);
        boolean[] used = bfs.getUsed();
        for (int i = 0; i < size; i++) {
            if (!used[i]) {
                return false;
            }
        }
        return true;
    }

    public Graph directedToUndirected() {
        Graph undirected = new Graph(rows, columns);
        for (int i = 0; i < size; i++) {
            Iterator<Edge> it = adj[i].iterator();
            while (it.hasNext()) {
                Edge edge = it.next();
                Vertex to = edge.getDestination();
                Vertex from = new Vertex(i);
                if (from.getNumber() < to.getNumber()) {
                    undirected.addEdge(from, to, 1);
                    undirected.addEdge(to, from, 1);
                }

            }
        }
        return undirected;
    }

    public int getSize() {
        return size;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public LinkedList[] getAdj() {
        return adj;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            adj[i].clear();
        }
    }

    public int countEdges(int i) {
        if (i == 0 || i == this.getColumns() - 1 || i == this.getSize() - 1 || i == this.getSize() - this.getColumns()) {
            return 2;
        } else if (i % this.getColumns() == 0 || i % this.getColumns() == this.getColumns() - 1 || (i > 0 && i < this.getColumns() - 1) || (i > this.getSize() - this.getColumns() && i < this.getSize() - 1)) {
            return 3;
        } else {
            return 4;
        }
    }
}
