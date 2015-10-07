import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vrbik on 01.10.2015.
 */
public class Launcher {

    public static void main(String[] args)
    {
        Vertex v0 = new Vertex("Harrisburg");
        Vertex v1 = new Vertex("Baltimore");
        Vertex v2 = new Vertex("Washington");
        Vertex v3 = new Vertex("Philadelphia");
        Vertex v4 = new Vertex("Binghamton");
        Vertex v5 = new Vertex("Allentown");
        Vertex v6 = new Vertex("New York");

        ArrayList<Vertex> vertexes = new ArrayList<>();

        vertexes.add(v0);
        vertexes.add(v1);
        vertexes.add(v2);
        vertexes.add(v3);
        vertexes.add(v4);
        vertexes.add(v5);
        vertexes.add(v6);

        ArrayList<Edge> edges = new ArrayList<>();

        edges.add(new Edge(v2, v1, 10));
        edges.add(new Edge(v3, v0, 3));
        edges.add(new Edge(v2, v3, 1));
        edges.add(new Edge(v4, v2, 100));
        edges.add(new Edge(v5, v4, 6));
        edges.add(new Edge(v6, v4, 30));
        edges.add(new Edge(v0, v6, 20));
        edges.add(new Edge(v3, v4, 5));

        //Dijkstra DJ = new Dijkstra();

        Dijkstra.computePaths(v5);

        System.out.println("Vzdalenost do v0: " + v0.minDistance);

        List<Vertex> path = Dijkstra.getShortestPathTo(v0);

        System.out.println("Cesta přes: " + path);
    }
}
