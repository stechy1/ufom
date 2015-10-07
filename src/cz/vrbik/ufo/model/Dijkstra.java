package cz.vrbik.ufo.model;

import cz.vrbik.ufo.model.galaxyobject.BasePlanet;
import cz.vrbik.ufo.model.galaxyobject.Path;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Dijkstra {
    public static void computePaths(BasePlanet source) {
        source.minDistance = 0.;
        PriorityQueue<BasePlanet> vertexQueue = new PriorityQueue<>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            BasePlanet u = vertexQueue.poll();

            for (Path e : u.paths) {
                BasePlanet v = e.getNeighbour(u);
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);

                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }

    public static List<BasePlanet> getShortestPathTo(BasePlanet target) {
        List<BasePlanet> path = new ArrayList<>();
        for (BasePlanet vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }

}
