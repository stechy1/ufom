package cz.vrbik.pt.semestralka.model.galaxy.generator;

import cz.vrbik.pt.semestralka.model.galaxy.Galaxy;
import cz.vrbik.pt.semestralka.model.galaxy.Path;
import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Planet;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Station;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Stream;

/**
 * Základní třída pro generátor galaxie
 */
public abstract class GalaxyGenerator {

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;

    private static final int NEIGHBOUR_SIZE = 30;
    private static final int AREA_INCREMENT = 30;

    private static boolean[][] occupation;
    private static BasePlanet[][] objectOccupation;


    public static final Random r = new Random(10);

    protected static int width;
    protected static int height;

    protected static ObservableList<Station> stations = FXCollections.observableArrayList();
    protected static ObservableList<Planet> planets = FXCollections.observableArrayList();
    protected static ObservableList<Path> paths = FXCollections.observableArrayList();
    /*protected static final List<Station> stations = new ArrayList<>();
    protected static final List<Planet> planets = new ArrayList<>();
    protected static final List<Path> paths = new ArrayList<>();*/

    /**
     * Nastaví šířku pro celý generátor
     *
     * @param w Šířka galaxie
     */
    public static void setWidth(int w) {
        width = w;
    }

    /**
     * Nastaví výšku pro celý generátor
     *
     * @param h Výška galaxie
     */
    public static void setHeight(int h) {
        height = h;
    }

    public static void bindStations(ObservableList<Station> stations) {
        GalaxyGenerator.stations = stations;
    }

    public static void bindPlanets(ObservableList<Planet> planets) {
        GalaxyGenerator.planets = planets;
    }

    public static void bindPaths(ObservableList<Path> paths) {
        GalaxyGenerator.paths = paths;
    }

    /**
     * Konstruktor třídy {@link GalaxyGenerator}
     */
    public GalaxyGenerator() {}

    /**
     * Konstruktor třídy {@link GalaxyGenerator}
     *
     * @param w Šířka galaxie využitelná generátorem
     * @param h Výška galaxie využitelná generátorem
     */
    public GalaxyGenerator(int w, int h) {
        setWidth(w);
        setHeight(h);

        clear();
    }

    /**
     * Zjišťuje obsazenost na zadané souřadnici
     *
     * @param x Vodorovná souřadnice
     * @param y Vertikální souřadnice
     * @return True, pokud jsou souřadnice obsazené, jinak false
     */
    protected boolean isOccupied(int x, int y) {
        return occupation[x][y];
    }

    /**
     * Označí okolí planety za obsazené
     *
     * @param x0 Vodorovná souřadnice levého horního rohu planety
     * @param y0 Horizontálí souřadnice levého horního rohu planety
     */
    protected void markArea(int x0, int y0, int spacing) {
        for (int i = -spacing; i < spacing; i++) {
            for (int j = -spacing; j < spacing; j++) {
                int x = x0 + i;
                int y = y0 + j;

                if (x >= 0 && x < width && y >= 0 && y < height) {
                    occupation[x][y] = true;
                }
            }
        }
    }

    /**
     * Metoda starající se o přidání všem planetám sousedy
     */
    protected void neighboursFinder() {

        for (Station station : stations)
            objectOccupation[(int) station.getX()][(int) station.getY()] = station;

        for (Planet planet : planets)
            objectOccupation[(int) planet.getX()][(int) planet.getY()] = planet;

        for (Station station : stations) {
            Map<Planet, Double> tmpPlanets = new HashMap<>();
            int size = NEIGHBOUR_SIZE;
            scanNeighbours(tmpPlanets, size, station, station.getFreeSlots());

            tmpPlanets = sortByValue(tmpPlanets);

            for (Planet planet : tmpPlanets.keySet()) {
                if (station.isFull())
                    break;
                paths.add(new Path(station, planet));
            }
        }


        List<Planet> rollingNeighbours= new ArrayList<>();

        for(int k = 25; k <= width; k+= 25) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < k; j++) {

                    BasePlanet tmp = objectOccupation[i][j];

                    if (tmp != null && !tmp.isFull()) {
                        rollingNeighbours.add((Planet) tmp);
                    }
                }
            }

            for (Planet planet : rollingNeighbours) {
                if (!planet.isFull()) {

                    Map<Planet, Double> tmpPlanets = new HashMap<>();

                    scanNeighbours(tmpPlanets, NEIGHBOUR_SIZE, planet, planet.getFreeSlots());

                    tmpPlanets = sortByValue(tmpPlanets);

                    for (Planet planet1 : tmpPlanets.keySet()) {
                        if (planet.isFull()) {
                            break;
                        }
                        paths.add(new Path(planet, planet1));
                    }
                    tmpPlanets.clear();
                }
            }
        }
        rollingNeighbours.clear();
    }

    /**
     * Náš vlastní komparátor pro setřídění mapy dle hodnot
     *
     * @param map Mapa k setřídění
     * @param <K> Klíče jsou reference planet
     * @param <V> Hodnoty jsou jejich vzdálenosti od domovské planety
     * @return Setříděnou mapu podle hodnot
     */
    protected <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
        Map<K,V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K,V>> st = map.entrySet().stream();
        st.sorted(Comparator.comparing(Map.Entry::getValue))
                .forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * Proskenuje všechny sousedy zadané planety
     *
     * @param map Mapa uchovávající všechny nalezené sousedy
     * @param neighbourSize Velikost prohledávaného okolí
     * @param basePlanet Výchozí planeta, pro kterou se hledají sousedi
     */
    protected void scanNeighbours(Map<Planet, Double> map, int neighbourSize, BasePlanet basePlanet, int count) {

        double x0 = basePlanet.getX();
        double y0 = basePlanet.getY();

        while(map.size() < count){
            map.clear();
            for (int i = -neighbourSize; i < neighbourSize; i++) {
                for (int j = 0 -neighbourSize; j < neighbourSize; j++) {
                    int x = (int) x0 + i;
                    int y = (int) y0 + j;

                    if (x >= 0 && x < width && y >= 0 && y < height) {
                        BasePlanet tmpPlanet = objectOccupation[x][y];
                        if (tmpPlanet != null && (tmpPlanet instanceof Planet) && !tmpPlanet.isFull() && tmpPlanet != basePlanet) {
                            map.put((Planet) tmpPlanet, basePlanet.planetDistance(tmpPlanet));
                        }
                    }
                }
            }

            if(neighbourSize > 900)
                break;

            neighbourSize += AREA_INCREMENT;
        }
    }

    /**
     * Vygeneruje objekty galaxie
     *
     * @param count Počet objektů
     * @param spacing Velikost prázdného okolního prostoru kolem každého objektu
     */
    public abstract void generate(int count, int spacing);

    /**
     * Vyrestartuje generátor do výchozího stavu
     */
    public void clear() {
        occupation = new boolean[width][height];
        objectOccupation = new BasePlanet[width][height];

        //TODO zajistit vyčištění objektů před ztracením referencí
        stations.clear();
        planets.clear();
        paths.clear();
    }

    /**
     * Vrátí referenci na list stanic
     */
    public List<Station> getStations() {
        return stations;
    }

    /**
     * Vrátí referenci na list planet
     */
    public List<Planet> getPlanets() {
        return planets;
    }

    /**
     * Vrátí referenci na list cest mezi planetami
     */
    public List<Path> getPaths() {
        return paths;
    }
}
