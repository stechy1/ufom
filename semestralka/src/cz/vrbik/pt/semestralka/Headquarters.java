package cz.vrbik.pt.semestralka;

import cz.vrbik.pt.semestralka.model.dijkstra.DijkstraAlgorithm;
import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Planet;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Station;
import cz.vrbik.pt.semestralka.model.service.Prepravka;
import cz.vrbik.pt.semestralka.model.service.RequestPriority;
import cz.vrbik.pt.semestralka.model.service.ResourceRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Třída představuje zdravotní velitelství
 */
public class Headquarters {

    private static final Logger log = Logger.getLogger(Headquarters.class.getName());

    public static final int CRICITAL_HIJACKED_SHIP = 10;
    private static final int TIME_RESERVE = 70;
    private static final int MAX_CARGO = 5000000;

    private static Headquarters INSTANCE;
    private ObservableList<Station> stations = FXCollections.observableArrayList();
    private ObservableList<Planet> planets = FXCollections.observableArrayList();
    //private ObservableList<Path> paths = FXCollections.observableArrayList();

    private final Set<ResourceRequest> requests = new HashSet<>();
    private final Set<ResourceRequest> pendingRequests = new HashSet<>();
    private final Map<BasePlanet, Prepravka> mapa = new HashMap<>();


    private int ticksToEndOfMonth;
    private int hijackedShips = 0;
    private int monthCounter = 0;


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
    private Headquarters() {
    }

    /**
     * Spustí dijkstrův algoritmus a ohodnotí graf
     */
    public void runDijkstra() {
        long start = System.currentTimeMillis();

        mapa.clear();

        for (Station station : stations) {
            DijkstraAlgorithm da = new DijkstraAlgorithm(station);

            for (Planet planet : planets) {

                if (planet.minDistance == Double.POSITIVE_INFINITY)
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
        if (!mapa.containsKey(request.requestPlanet))
            return;

        if ((request.requestPlanet.isNotInteresting() || (mapa.get(request.requestPlanet).weight + TIME_RESERVE) > ticksToEndOfMonth) && request.priority == RequestPriority.NORMAL) {
            //log.info("Odmitnuti požadavku na léky planety " + request.requestPlanet.getName() + " nedostatek obyvatel|nedostatek času k doručení \n" +
            //        "požadovana delka: " + (mapa.get(request.requestPlanet).weight + TIME_RESERVE) + " vs zbyly cas" + ticksToEndOfMonth);

            //log.info("STAV POPULACE : "  + request.requestPlanet.inhabitants + " isnpteinteresting=" + request.requestPlanet.isNotInteresting());

            return;
        }

        if (request.quantity > MAX_CARGO) {
            requests.add(new ResourceRequest(request.requestPlanet, (request.quantity - MAX_CARGO), request.priority));
            request.quantity = MAX_CARGO;
        }

        requests.add(request);

    }

    /**
     * Nabinduje referenci na stanice
     *
     * @param stations Reference na stanice
     */
    public void bindStations(ObservableList<Station> stations) {
        this.stations = stations;
    }

    /**
     * Nabinduje referenci na planety
     *
     * @param planets Reference na seznam planet
     */
    public void bindPlanets(ObservableList<Planet> planets) {
        this.planets = planets;
    }

    private void monthUpdate() {

        long deaths = 0;
        long alives = 0;
        int deathPlanets = 0;

        for (Planet a : planets) {
            deaths += a.graves;
            alives += a.inhabitants;
            if (a.isNotInteresting()) {
                deathPlanets++;
            }
        }

        DecimalFormat formatter = new DecimalFormat("#,###");

        log.info("\n---------- " + monthCounter + ". MĚSÍČNÍ HLÁŠENÍ----------\n" +
                "počet přepadení piráty: " + hijackedShips + "\n" +
                "počet živých celkově: " + formatter.format(alives) + "\n" +
                "počet mrtvých celkově: " + formatter.format(deaths) + "\n" +
                "počet nezajímavých planet < 40k: " + deathPlanets + "\n");

        hijackedShips = 0;
        deathPlanets = 0;

        /*
        if (monthCounter == 12) {
            for (Planet a : planets) {
                log.info("historie populace na planetě: " + a.getName() + " " + a.inhabitantStatistics);
                log.info("historie dodávek na planetě: " + a.getName() + " " + a.deliversStatistics + "\n");
            }
        }*/


    }

    public void update(int timestamp) {
        ticksToEndOfMonth = ((30 * 25) - (timestamp % (30 * 25)));

        if ((timestamp % (30 * 25)) == 0 && timestamp != 0) {
            requests.clear();
            monthCounter++;
        }

        if ((timestamp % (30 * 25 + 1)) == 0) {
            monthUpdate();
        }


        if ((hijackedShips % 10) == 0 && hijackedShips != 0) {
            runDijkstra();
        }

        if (requests.size() == 0)
            return;


        Iterator<ResourceRequest> requestIterator = requests.iterator();
        int i = 0;
        while (requestIterator.hasNext()) {
            if (i == 300)
                break;

            ResourceRequest request = requestIterator.next();

            Prepravka p = mapa.get(request.requestPlanet);
            if (p != null) {
                Station s = (Station) p.source;
                if (s.sendShipToTrip(p.path, request))
                    requestIterator.remove();
            } else {
                log.warn("Nalezena nedosažitelná planeta: " + request.requestPlanet.getName());
            }
            i++;

        }

    }
}

