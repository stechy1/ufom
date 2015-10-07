package cz.vrbik.pt.semestralka;

import cz.vrbik.pt.semestralka.model.dijkstra.DijkstraAlgorithm;
import cz.vrbik.pt.semestralka.model.galaxy.Galaxy;
import cz.vrbik.pt.semestralka.model.galaxy.Path;
import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Planet;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Station;
import cz.vrbik.pt.semestralka.model.service.Prepravka;
import cz.vrbik.pt.semestralka.model.service.ResourceRequest;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Třída představuje zdravotní velitelství
 */
public class Headquarters {

    private static final Logger log = Logger.getLogger(Headquarters.class.getName());

    public static final int CRICITAL_HIJACKED_SHIP = 10;

    private static Headquarters INSTANCE;
    private List<Station> stations = new ArrayList<>();
    private List<Planet> planets = new ArrayList<>();
    private List<Path> paths = new ArrayList<>();

    private final Queue<ResourceRequest> requests = new PriorityQueue<>();
    private final Map<BasePlanet, Prepravka> mapa = new HashMap<>();

    private int hijackedShips = 0;

    /**
     * Vrátí referenci na {@link Headquarters}
     */
    public static Headquarters getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Headquarters();
        return INSTANCE;
    }

    /**
     * Konstruktor třídy {@link Headquarters}
     */
    private Headquarters() {}

    //    Dočasná metoda pro vytvoření testovacího requestu
    public void makeRequest() {
        ResourceRequest request = new ResourceRequest(planets.get(256), 60000);
        makeRequest(request);

    }

    /**
     * Spustí dijkstrův algoritmus a ohodnotí graf
     */
    public void runDijkstra() {

        log.info("Spouštím dijkstův algoritmus pro přepočítání tras...");
        long start = System.currentTimeMillis();

        mapa.clear();

        for (Station station : stations) {
            DijkstraAlgorithm da = new DijkstraAlgorithm(station);

            for (Planet planet : planets) {

                if(planet.minDistance == Double.POSITIVE_INFINITY)
                    continue;

                Prepravka nova = new Prepravka(station, da.getShortestPathTo(planet), planet.minDistance);
                    if (mapa.get(planet) != null) {
                    Prepravka tmp = mapa.get(planet);
                    if (nova.weight < tmp.weight)
                        mapa.put(planet, nova);
                } else {
                    mapa.put(planet, nova);
                }
            }
            setDefaults();
        }

        long delta = System.currentTimeMillis() - start;
        log.info("Dijkstra přepočítal trasy za: " + delta + "ms.");
    }

    public void setDefaults() {
        for (Station a : stations) {
            a.minDistance = Double.POSITIVE_INFINITY;
            a.previous = null;
        }

        for (Planet a : planets) {
            a.minDistance = Double.POSITIVE_INFINITY;
            a.previous = null;
        }
    }

    /**
     * Nahlásí další přepadenou loď
     */
    public void nextHijackedShip() {
        hijackedShips++;
        log.debug("Přidávám další záznam o napadené lodi piráty");
    }

    /**
     * Vytvoří nový požadavek na zásoby
     *
     * @param request Reference na požadavek
     */
    public void makeRequest(ResourceRequest request) {
        requests.add(request);
    }

    /**
     * Nabinduje referenci na stanice
     *
     * @param stations Reference na stanice
     */
    public void bindStations(List<Station> stations) {
        this.stations = stations;
    }

    /**
     * Nabinduje referenci na planety
     *
     * @param planets Reference na seznam planet
     */
    public void bindPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    /**
     * Nabinduje referenci na cesty
     *
     * @param paths Reference na seznam cest
     */
    public void bindPaths(List<Path> paths) {
        this.paths = paths;
    }

    public void update(int timestamp) {
        /*
        if (hijackedShips >= CRICITAL_HIJACKED_SHIP) {
            hijackedShips = 0;
            runDijkstra();
        }*/

        if (requests.size() == 0)
            return;

        ResourceRequest request = requests.poll();

        Prepravka p = mapa.get(request.requestPlanet);
        Station s = (Station) p.source;

        s.sendShipToTrip(p.path);
    }
}
