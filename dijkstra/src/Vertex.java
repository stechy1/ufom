import java.util.ArrayList;

class Vertex implements Comparable<Vertex> {

    public final String name;
    public ArrayList<Edge> paths;

    public double minDistance = Double.POSITIVE_INFINITY;

    public Vertex previous;

    public Vertex(String argName) {
        this.paths = new ArrayList<>();
        name = argName;
    }

    public String toString() {
        return name;
    }
    public int compareTo(Vertex other) {
        return Double.compare(minDistance, other.minDistance);
    }

}