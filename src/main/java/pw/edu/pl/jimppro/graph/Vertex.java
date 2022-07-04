package pw.edu.pl.jimppro.graph;

public class Vertex {

    private int number;

    public Vertex(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vertex vertex = (Vertex) o;
        return number == vertex.number;
    }

    @Override
    public int hashCode() {
        return number;
    }

    public int getNumber() {
        return number;
    }


}
