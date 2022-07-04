package pw.edu.pl.jimppro.graph;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pw.edu.pl.jimppro.gui.GraphController;

import java.util.*;

public class Path {
    private final Vertex from;
    private final Vertex to;
    private final SimpleStringProperty fromTo;
    private List<Vertex> pathList;
    private CheckBox showPath;
    private CheckBox removePath;
    private RadioButton showWeights;
    private RadioButton doNotShowWeights;
    private final Graph graph;
    private final Color color;

    public Path(Vertex from, Vertex to, Graph graph) {
        this.from = from;
        this.to = to;
        this.fromTo = new SimpleStringProperty(from.getNumber() + " -> " + to.getNumber());
        this.showPath = new CheckBox();
        this.removePath = new CheckBox();
        this.showWeights = new RadioButton();
        this.doNotShowWeights = new RadioButton();
        this.pathList = new LinkedList<>();
        this.graph = graph;
        showPath.setSelected(true);
        doNotShowWeights.setSelected(true);
        ToggleGroup weightGroup = new ToggleGroup();
        showWeights.setToggleGroup(weightGroup);
        doNotShowWeights.setToggleGroup(weightGroup);
        removePath.setAlignment(Pos.CENTER);
        showPath.setAlignment(Pos.CENTER);
        showWeights.setAlignment(Pos.CENTER);
        doNotShowWeights.setAlignment(Pos.CENTER);
        Random random = new Random();
        int r = random.nextInt(130) + 90;
        int g = random.nextInt(130) + 90;
        int b = random.nextInt(130) + 90;
        color = Color.rgb(r, g, b);
    }

    public LinkedList<Vertex> getPathList() {
        return (LinkedList<Vertex>) pathList;
    }

    public void setPathList(List<Vertex> list) {
        this.pathList = list;
    }

    public void addToPathList(Vertex numOfVertex) {
        this.pathList.add(numOfVertex);
    }

    public String getFromTo() {
        return fromTo.get();
    }

    public void setFromTo(Vertex from, Vertex to) {
        this.fromTo.set(from.getNumber() + " -> " + to.getNumber());
    }

    public CheckBox getShowPath() {
        return showPath;
    }

    public void setShowPath(CheckBox check) {
        this.showPath = check;
    }

    public CheckBox getRemovePath() {
        return removePath;
    }

    public void setRemovePath(CheckBox check) {
        this.removePath = check;
    }

    public RadioButton getShowWeights() {
        return showWeights;
    }

    public void setShowWeights(RadioButton radio) {
        this.showWeights = radio;
    }

    public RadioButton getDoNotShowWeights() {
        return doNotShowWeights;
    }

    public void setDoNotShowWeights(RadioButton radio) {
        this.doNotShowWeights = radio;
    }

    public Color getColor() {
        return color;
    }

    public void display(Canvas canvas, boolean showWeights) {
        double[] offsets = GraphController.offsets;
        double[] offsetsRight = GraphController.offsetsRight;
        double[] offsetsLeft = GraphController.offsetsLeft;
        double[] offsetsDown = GraphController.offsetsDown;
        double[] offsetsUp = GraphController.offsetsUp;
        GraphicsContext grc = canvas.getGraphicsContext2D();
        if (pathList.size() == 0 || graph.getSize() == 0) {
            return;
        }
        Vertex cur = from;
        int x = cur.getNumber() % graph.getColumns();
        int y = cur.getNumber() / graph.getColumns();
        grc.setStroke(color);
        grc.setLineWidth(0.05);
        grc.strokeOval(0.3 + x + offsets[cur.getNumber()] / 2.0, 0.3 + y + offsets[cur.getNumber()] / 2.0, 0.5 - offsets[cur.getNumber()], 0.5 - offsets[cur.getNumber()]);
        Iterator<Vertex> it = pathList.iterator();
        grc.setLineWidth(0.03);
        grc.setFont(new Font("10", 0.1));
        LinkedList<Edge>[] adj = graph.getAdj();
        while (it.hasNext()) {
            Vertex next = it.next();
            x = cur.getNumber() % graph.getColumns();
            y = cur.getNumber() / graph.getColumns();
            int xTo = next.getNumber() % graph.getColumns();
            int yTo = next.getNumber() / graph.getColumns();
            double weight = 0;
            if (showWeights) {
                for (Edge edge : adj[cur.getNumber()]) {
                    if (edge.getDestination().equals(next)) {
                        weight = edge.getWeight();
                        break;
                    }
                }
            }
            if (x + 1 == xTo) {
                grc.strokeLine(0.3 + x + 0.5, 0.3 + y + 0.25, 0.3 + x + 1 - offsetsRight[cur.getNumber()], 0.3 + y + 0.25);
                grc.strokeLine(0.3 + x + 0.85 - offsetsRight[cur.getNumber()], 0.3 + y + 0.35, 0.3 + x + 1 - offsetsRight[cur.getNumber()], 0.3 + y + 0.25);
                grc.strokeLine(0.3 + x + 0.85 - offsetsRight[cur.getNumber()], 0.3 + y + 0.15, 0.3 + x + 1 - offsetsRight[cur.getNumber()], 0.3 + y + 0.25);
                if (showWeights) {
                    grc.setLineWidth(0.01);
                    grc.strokeText(Double.toString(weight), (0.25 + 2 * x + 1.5) / 2, 0.3 + y + 0.1);
                    grc.setLineWidth(0.03);
                }
                offsetsRight[cur.getNumber()] += 0.05;
                if (offsetsRight[cur.getNumber()] >= 0.15) {
                    offsetsRight[cur.getNumber()] = 0;
                }
            }
            if (y + 1 == yTo) {
                grc.strokeLine(0.3 + x + 0.25, 0.3 + y + 0.5, 0.3 + x + 0.25, 0.3 + y + 1 - offsetsDown[cur.getNumber()]);
                grc.strokeLine(0.3 + x + 0.35, 0.3 + y + 0.85 - offsetsDown[cur.getNumber()], 0.3 + x + 0.25, 0.3 + y + 1 - offsetsDown[cur.getNumber()]);
                grc.strokeLine(0.3 + x + 0.15, 0.3 + y + 0.85 - offsetsDown[cur.getNumber()], 0.3 + x + 0.25, 0.3 + y + 1 - offsetsDown[cur.getNumber()]);
                if (showWeights) {
                    grc.setLineWidth(0.01);
                    grc.strokeText(Double.toString(weight), (0.13 + x), (0.55 + 2 * y + 1.5) / 2);
                    grc.setLineWidth(0.03);
                }
                offsetsDown[cur.getNumber()] += 0.05;
                if (offsetsDown[cur.getNumber()] >= 0.15) {
                    offsetsDown[cur.getNumber()] = 0;
                }
            }
            if (x - 1 == xTo) {
                grc.strokeLine(0.3 + xTo + 0.5 + offsetsLeft[cur.getNumber()], 0.3 + yTo + 0.25, 0.3 + xTo + 1, 0.3 + yTo + 0.25);
                grc.strokeLine(0.3 + xTo + 0.5 + offsetsLeft[cur.getNumber()], 0.3 + yTo + 0.25, 0.3 + xTo + 0.65 + offsetsLeft[cur.getNumber()], 0.3 + yTo + 0.35);
                grc.strokeLine(0.3 + xTo + 0.5 + offsetsLeft[cur.getNumber()], 0.3 + yTo + 0.25, 0.3 + xTo + 0.65 + offsetsLeft[cur.getNumber()], 0.3 + yTo + 0.15);
                if (showWeights) {
                    grc.setLineWidth(0.01);
                    grc.strokeText(Double.toString(weight), (0.18 + 2 * xTo + 1.5) / 2, 0.3 + yTo + 0.5);
                    grc.setLineWidth(0.03);
                }
                offsetsLeft[cur.getNumber()] += 0.05;
                if (offsetsLeft[cur.getNumber()] >= 0.15) {
                    offsetsLeft[cur.getNumber()] = 0;
                }
            }
            if (y - 1 == yTo) {
                grc.strokeLine(0.3 + xTo + 0.25, 0.3 + yTo + 0.5 + offsetsUp[cur.getNumber()], 0.3 + xTo + 0.25, 0.3 + yTo + 1);
                grc.strokeLine(0.3 + xTo + 0.25, 0.3 + yTo + 0.5 + offsetsUp[cur.getNumber()], 0.3 + xTo + 0.15, 0.3 + yTo + 0.65 + offsetsUp[cur.getNumber()]);
                grc.strokeLine(0.3 + xTo + 0.25, 0.3 + yTo + 0.5 + offsetsUp[cur.getNumber()], 0.3 + xTo + 0.35, 0.3 + yTo + 0.65 + offsetsUp[cur.getNumber()]);
                if (showWeights) {
                    grc.setLineWidth(0.01);
                    grc.strokeText(Double.toString(weight), (0.42 + xTo + 0.3), (0.55 + 2 * yTo + 1.5) / 2);
                    grc.setLineWidth(0.03);
                }
                offsetsUp[cur.getNumber()] += 0.05;
                if (offsetsUp[cur.getNumber()] >= 0.15) {
                    offsetsUp[cur.getNumber()] = 0;
                }
            }
            cur = next;
        }
        x = cur.getNumber() % graph.getColumns();
        y = cur.getNumber() / graph.getColumns();
        grc.setLineWidth(0.05);
        grc.strokeOval(0.3 + x + offsets[cur.getNumber()] / 2.0, 0.3 + y + offsets[cur.getNumber()] / 2.0, 0.5 - offsets[cur.getNumber()], 0.5 - offsets[cur.getNumber()]);
        grc.setLineWidth(0.03);
        grc.setStroke(Color.BLACK);
        offsets[from.getNumber()] += 0.1;
        if (!from.equals(to)) {
            offsets[to.getNumber()] += 0.1;
        }
        if (offsets[from.getNumber()] >= 0.3) {
            offsets[from.getNumber()] = 0;
        }
        if (offsets[to.getNumber()] >= 0.3) {
            offsets[to.getNumber()] = 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return from.equals(path.from) && to.equals(path.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
