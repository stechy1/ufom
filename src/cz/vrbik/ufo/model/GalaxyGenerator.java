package cz.vrbik.ufo.model;

import cz.vrbik.ufo.model.galaxyobject.BasePlanet;
import cz.vrbik.ufo.model.galaxyobject.Path;
import cz.vrbik.ufo.model.galaxyobject.Station;
import cz.vrbik.ufo.model.galaxyobject.Planet;

import java.util.*;
import java.util.stream.Stream;

/**
 * Třída představující generátor planet
 */
public class GalaxyGenerator {

    private Galaxy galaxy;

    private static final int NEIGHBOUR_SIZE = 30;
    private static final int AREA_INCREMENT = 30;

    private int width;
    private int height;
    private Random r = new Random();

    private final boolean[][] occupation;
    private BasePlanet[][] objectOccupation;

    private final List<Station> stations = new ArrayList<>();
    private final List<Planet> planets = new ArrayList<>();
    private final List<Path> paths = new ArrayList<>();

    /**
     * Konstruktor generátoru galaxie
     * @param galaxy Reference na danou galaxii
     */
    public GalaxyGenerator(Galaxy galaxy) {
        this.galaxy = galaxy;
        width = Galaxy.GALAXY_WIDTH;
        height = Galaxy.GALAXY_HEIGHT;

        occupation = new boolean[width][height];
        objectOccupation = new BasePlanet[width][height];

    }

    //TODO zeptat se na navrhovy vzor, abychom zabránili zdvojenému kódu
    public void generateStations(int count, int spacing) {

        for (int i = 0; i < count; i++) {
            Station s = generateStation();
            stations.add(s);
            markArea((int)s.getX(), (int)s.getY(), spacing);
        }
    }

    private Station generateStation(){
        int x = r.nextInt(width);
        int y = r.nextInt(height);

        if (isOccupied(x, y))
            return generateStation();
        else {
            return new Station(x, y, galaxy);
        }
    }

    public void generatePlanets(int count, int spacing) {


        for (int i = 0; i < count; i++) {
            Planet p = generatePlanet();
            planets.add(p);
            markArea((int)p.getX(), (int)p.getY(), spacing);
        }

        neighboursFinder();

        System.out.println("Počet cest : " + paths.size());
    }

    private Planet generatePlanet() {
        int x = r.nextInt(width);
        int y = r.nextInt(height);

        if (isOccupied(x, y)) {
            return generatePlanet();
        }

        else {
            return new Planet(x, y, galaxy);
        }
    }

    /**
     * Zjišťuje obsazenost na zadané souřadnici
     * @param x Vodorovná souřadnice
     * @param y Vertikální souřadnice
     * @return True, pokud jsou souřadnice obsazené, jinak false
     */
    private boolean isOccupied(int x, int y) {
        return occupation[x][y];
    }

    /**
     * Označí okolí planety za obsazené
     * @param x0 Vodorovná souřadnice levého horního rohu planety
     * @param y0 Horizontálí souřadnice levého horního rohu planety
     */
    private void markArea(int x0, int y0, int spacing) {
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
    private void neighboursFinder() {

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
     * @param map Mapa k setřídění
     * @param <K> Klíče jsou reference planet
     * @param <V> Hodnoty jsou jejich vzdálenosti od domovské planety
     * @return Setříděnou mapu podle hodnot
     */
    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
        Map<K,V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K,V>> st = map.entrySet().stream();
        st.sorted(Comparator.comparing(Map.Entry::getValue))
                .forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * Proskenuje všechny sousedy zadané planety
     * @param map Mapa uchovávající všechny nalezené sousedy
     * @param neighbourSize Velikost prohledávaného okolí
     * @param basePlanet Výchozí planeta, pro kterou se hledají sousedi
     */
    private void scanNeighbours(Map<Planet, Double> map, int neighbourSize, BasePlanet basePlanet, int count) {

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
                            map.put((Planet) tmpPlanet, planetDistance(basePlanet, tmpPlanet));
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
     * Vrátí vzdálenost dvou planet
     * @param a Planeta A První planeta
     * @param b Planeta B Druhá planeta
     * @return vzdálenost od A do B
     */
    public double planetDistance(BasePlanet a, BasePlanet b) {
        return Math.sqrt(Math.pow((b.getX() - a.getX()), 2) + Math.pow((b.getY() - a.getY()), 2));
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public List<Path> getPaths() { return paths;}
}
