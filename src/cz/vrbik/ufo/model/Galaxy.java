package cz.vrbik.ufo.model;

import cz.vrbik.ufo.model.galaxyobject.BasePlanet;
import cz.vrbik.ufo.model.galaxyobject.Path;
import cz.vrbik.ufo.model.galaxyobject.Station;
import cz.vrbik.ufo.model.galaxyobject.Planet;
import javafx.scene.canvas.GraphicsContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Třída představující galaxii
 */
public class Galaxy {

    public static int PLANET_COUNT = 5000;
    public static int GALAXY_WIDTH = 800;
    public static int GALAXY_HEIGHT = 800;
    public static int PLANET_SPACING = 3;
    public static int STATION_SPACING = 10;
    public static int STATION_COUNT = 5;


    private GalaxyGenerator generator;
    private Config config;

    public final List<Station> stations;
    public final List<Planet> planets;
    public final List<Path> paths;

    /**
     * Konstruktor galaxie
     * @param config Reference na aktuální konfiguraci
     */
    public Galaxy(Config config) {
        this.config = config;

        stations = new ArrayList<>();
        planets = new ArrayList<>();
        paths = new ArrayList<>();
    }

    /**
     * Přečte aktuální nastavení galaxie
     */
    private void updateGalaxyConfig() {
        PLANET_COUNT = config.getPlanetCount();
        GALAXY_WIDTH = config.getGalaxyWidth();
        GALAXY_HEIGHT = config.getGalaxyHeight();
        PLANET_SPACING = config.getPlanetSpacing();
        STATION_SPACING = config.getStationSpacing();
        STATION_COUNT = config.getStationCount();
    }

    /**
     * Vygeneruje novou galaxii
     * Spustí galaxy generátor
     */
    public void generate() {
        updateGalaxyConfig();
        generator = new GalaxyGenerator(this);
        generator.generateStations(STATION_COUNT, STATION_SPACING);
        generator.generatePlanets(PLANET_COUNT, PLANET_SPACING);

        stations.addAll(generator.getStations());
        planets.addAll(generator.getPlanets());
        paths.addAll(generator.getPaths());

        System.out.printf("Vygenerováno %d planet%n", PLANET_COUNT);



        List<BasePlanet> all = new ArrayList<>();
        all.addAll(planets);
        all.addAll(stations);

        long start = System.currentTimeMillis();
        Dijkstra.computePaths(all.get(5001));


        ArrayList<List<BasePlanet>> result = new ArrayList<List<BasePlanet>>();

        for(BasePlanet a : all){
                    result.add(Dijkstra.getShortestPathTo(a));
        }

        System.out.println("čas DJ " + (System.currentTimeMillis() - start));

        System.out.println(result);

        /*
        for (int i = 0; i < 1000; i++) {
            System.out.println(Dijkstra.getShortestPathTo(all.get(1)).size());
        }*/
    }

    /**
     * Vyrenderuje galaxii
     * @param g Reference na grafický kontext
     */
    public void render(GraphicsContext g) {


        for (Planet planet : planets) {
            planet.render(g);

        }


        for (Station station : stations) {
            station.render(g);
        }


        for (Path path : paths){
            path.render(g);
        }

    }

    /**
     * Velký třesk, který uklidí galaxii
     */
    public void bigBang() {

        stations.clear();
        planets.clear();
        paths.clear();

        Station.resetCounter();
        Planet.resetCounter();
    }

    /**
     * Vyexportuje celou galaxii do souboru
     * Soubor nemusíme uzavírat, o to se nám postará blok try/catch
     */
    public boolean exportGalaxy(File exportFile) {

        try (PrintWriter pw = new PrintWriter(exportFile)) {
            pw.println(config);
            planets.forEach(pw::println);
            stations.forEach(pw::println);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Naimportuje galaxii ze souboru
     */
    public boolean importGalaxy(File importFile) {
        bigBang();
        try (BufferedReader reader = new BufferedReader(new FileReader(importFile))) {

            String configLine = reader.readLine();
            config.updateConfig(configLine);
            updateGalaxyConfig();

            for (int i = 0; i < PLANET_COUNT; i++) {
                String line = reader.readLine();
                planets.add(Planet.getPlanetFromString(line, this));
            }

            for (int i = 0; i < STATION_COUNT; i++) {
                String line = reader.readLine();
                stations.add(Station.getStationFromString(line, this));
            }

            //stations.forEach(Station::connectNeighbours);
            //planets.forEach(Planet::connectNeighbours);

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
