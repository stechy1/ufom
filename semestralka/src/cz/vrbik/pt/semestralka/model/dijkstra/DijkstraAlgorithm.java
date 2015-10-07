package cz.vrbik.pt.semestralka.model.dijkstra;

import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;

import java.util.*;

/**
 * Třída představující dijkstrův algoritmus.
 * Algoritmus ohodnotí graf a vrátí nejkratší cesty k sousedům
 */
public class DijkstraAlgorithm {

    private BasePlanet source;

    /**
     * Konstruktor třídy {@link DijkstraAlgorithm}
     *
     * @param homePlanet Reference na výchozí planetu, odkud se bude graf hodnotit
     */
    public DijkstraAlgorithm(BasePlanet homePlanet) {
        source = homePlanet;
        computePaths();
    }

    /**
     * Ohodnotí graf z výchozího bodu
     */
    public void computePaths() { 
        source.minDistance = 0;
        PriorityQueue<BasePlanet> vertexQueue = new PriorityQueue<>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            BasePlanet u = vertexQueue.poll();

            u.paths.stream().filter(e -> !e.isPirateDetected()).forEach(e -> {
                BasePlanet v = e.getNeighbour(u);
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                assert v != null;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);
                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            });
        }
    }

    /**
     * Vrátí nejkratší cestu z ohodnoceného grafu do cíle
     *
     * @param target Cíl cesty
     * @return referenci na list obsahující cestu k cíli
     */
    public LinkedList<BasePlanet> getShortestPathTo(BasePlanet target) {
        LinkedList<BasePlanet> path = new LinkedList<>();
        for (BasePlanet vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }
}
