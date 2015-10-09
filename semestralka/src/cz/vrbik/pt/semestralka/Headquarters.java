package cz.vrbik.pt.semestralka;

import cz.vrbik.pt.semestralka.model.dijkstra.DijkstraAlgorithm;
import cz.vrbik.pt.semestralka.model.galaxy.Path;
import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Planet;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Station;
import cz.vrbik.pt.semestralka.model.service.Prepravka;
import cz.vrbik.pt.semestralka.model.service.ResourceRequest;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Třída představuje zdravotní velitelství
 */
public class Headquarters {

    private static final Logger log = Logger.getLogger(Headquarters.class.getName());

    public static final int CRICITAL_HIJACKED_SHIP = 10;
    private static final int TIME_RESERVE = 60;

    private static Headquarters INSTANCE;
    private List<Station> stations = new ArrayList<>();
    private List<Planet> planets = new ArrayList<>();
    private List<Path> paths = new ArrayList<>();

    private final Set<ResourceRequest> requests = new HashSet<>();
    private final Set<ResourceRequest> pendingRequests = new HashSet<>();
    private final Map<BasePlanet, Prepravka> mapa = new HashMap<>();


    private int ticksToEndOfMonth;
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

    /**
     * Spustí dijkstrův algoritmus a ohodnotí graf
     */
    public void runDijkstra() {
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
    }

    /**
     * Vytvoří nový požadavek na zásoby
     *
     * @param request Reference na požadavek
     */
    public void makeRequest(ResourceRequest request) {

        //FIREWALL proti už nevalidním requestům

        if (request.requestPlanet.isInteresting() || (mapa.get(request.requestPlanet).weight + TIME_RESERVE) > ticksToEndOfMonth) {
            log.info("Odmitnuti požadavku na léky planety " + request.requestPlanet.getName() + " nedostatek obyvatel|nedostatek času k doručení \n" +
                    "" + (mapa.get(request.requestPlanet).weight + TIME_RESERVE) + " vs " + ticksToEndOfMonth +
                    "");
            return;
        }


        /* už není potřeba
        if (requests.contains(request))
            requests.remove(request);*/


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

    private void monthUpdate(){

        int deaths = 0;

        for(Planet a : planets){
            deaths += a.graves;
        }


        log.info("----------MĚSÍČNÍ HLÁŠENÍ----------\n" +
                "počet přepadení piráty: " + hijackedShips + "\n" +
                "počet mrtvých celkově: " + deaths);

        hijackedShips = 0;

    }

    public void update(int timestamp) {
         ticksToEndOfMonth = ((30*25) - (timestamp % (30*25)));

        if((timestamp % (30*25)) == 0 && timestamp != 0){
            requests.clear();
        }

        if((timestamp % (30*25 + 1)) == 0 && timestamp != 0){
            monthUpdate();
        }

        if (requests.size() == 0)
            return;


        Iterator<ResourceRequest> requestIterator = requests.iterator();
        int i = 0;
        while (requestIterator.hasNext()) {
            if (i == 50)
                break;

            ResourceRequest request = requestIterator.next();
                Prepravka p = mapa.get(request.requestPlanet);

                Station s = (Station) p.source;
                if (s.sendShipToTrip(p.path, request))
                    requestIterator.remove();

                i++;
            }

        }
    }

