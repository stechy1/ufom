/**
 * Created by Vrbik on 01.10.2015.
 */
class Edge {
    public final Vertex a;
    public final Vertex b;
    public final double weight;

    public Edge(Vertex a, Vertex b, double weight) {
        this.a = a;
        this.b = b;

        a.paths.add(this);
        b.paths.add(this);

        this.weight = weight;
    }

    public Vertex getNeighbour(Vertex x) {
        if(x == a) {
            return b;
        }
        else if(x == b) {
            return a;
        }

        return null;
    }
}
