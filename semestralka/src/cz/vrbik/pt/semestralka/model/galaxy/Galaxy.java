package cz.vrbik.pt.semestralka.model.galaxy;

import cz.vrbik.pt.semestralka.Headquarters;
import cz.vrbik.pt.semestralka.model.IRestorable;
import cz.vrbik.pt.semestralka.model.IUpdatable;
import cz.vrbik.pt.semestralka.model.galaxy.generator.GalaxyGenerator;
import cz.vrbik.pt.semestralka.model.galaxy.generator.PlanetGenerator;
import cz.vrbik.pt.semestralka.model.galaxy.generator.StationGenerator;
import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Planet;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Station;
import cz.vrbik.pt.semestralka.model.service.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída představující jednu galaxii
 */
public class Galaxy implements IUpdatable, IRestorable {

    private static final Logger log = Logger.getLogger(Galaxy.class.getName());

    public static int TRANSLATE_X = 50;
    public static int TRANSLATE_Y = 50;

    public final Config config;

    private final GalaxyGenerator stationGenerator;
    private final GalaxyGenerator planetGenerator;
    private final Headquarters headquarters = Headquarters.getInstance();

    private final ObservableList<Station> stations = FXCollections.observableArrayList();
    private final ObservableList<Planet> planets = FXCollections.observableArrayList();
    private final ObservableList<Path> paths = FXCollections.observableArrayList();

    public int PLANET_COUNT = 5000;
    public int GALAXY_WIDTH = 800;
    public int GALAXY_HEIGHT = 800;
    public int PLANET_SPACING = 3;
    public int STATION_SPACING = 10;
    public int STATION_COUNT = 5;

    /**
     * Konstruktor třídy {@link Galaxy}
     *
     * @param config Reference na konfiguraci {@link Config}
     */
    public Galaxy(Config config) {
        this.config = config;

        GalaxyGenerator.setWidth(GALAXY_WIDTH);
        GalaxyGenerator.setHeight(GALAXY_HEIGHT);
        GalaxyGenerator.bindStations(stations);
        GalaxyGenerator.bindPlanets(planets);
        GalaxyGenerator.bindPaths(paths);

        stationGenerator = new StationGenerator();
        planetGenerator = new PlanetGenerator();

        headquarters.bindStations(stations);
        headquarters.bindPlanets(planets);
    }

    /**
     * Aktualizuje nastavení galaxie z konfigu
     */
    private void updateGalaxyConfig() {
        PLANET_COUNT = config.getPlanetCount();
        GALAXY_WIDTH = config.getGalaxyWidth();
        GALAXY_HEIGHT = config.getGalaxyHeight();
        PLANET_SPACING = config.getPlanetSpacing();
        STATION_SPACING = config.getStationSpacing();
        STATION_COUNT = config.getStationCount();
        GalaxyGenerator.setWidth(GALAXY_WIDTH);
        GalaxyGenerator.setHeight(GALAXY_HEIGHT);
    }

    /**
     * Metoda která zajistí znovu nastavení galaxie
     */
    public void bigBang() {
        stationGenerator.clear();
        Planet.resetCounter();
        Station.resetCounter();
    }

    /**
     * Metoda vygeneruje novou galaxii
     */
    public void generate() {
        log.info("Generuji novou galaxii..");
        config.saveConfig();
        bigBang();
        updateGalaxyConfig();

        long start = System.currentTimeMillis();
        stationGenerator.generate(STATION_COUNT, STATION_SPACING);
        planetGenerator.generate(PLANET_COUNT, PLANET_SPACING);

        headquarters.runDijkstra();

        long delta = System.currentTimeMillis() - start;
        log.info(String.format("Galaxie byla vygenerována za: %d ms.", delta));

    }

    /**
     * Metoda aktualizující logiku objektu
     *
     * @param timestamp Doba od spuštění simulace
     */
    @Override
    public void update(int timestamp) {
        log.debug(String.format("Update číslo: %d", timestamp));
        headquarters.update(timestamp);

        paths.forEach(path -> path.update(timestamp));
        planets.forEach(planet -> planet.update(timestamp));
        stations.forEach(station -> station.update(timestamp));
    }

    /**
     * Metoda vykreslující objekt
     *
     * @param g Reference na grafický kontext
     */
    @Override
    public void render(GraphicsContext g) {

        paths.forEach(path -> path.render(g));
        planets.forEach(planet -> planet.render(g));
        stations.forEach(station -> station.render(g));
    }

    /**
     * Metoda uloží objekt do souboru
     *
     * @param file Reference na ukládaný soubor
     */
    @Override
    public boolean store(File file) {

        try (PrintWriter pw = new PrintWriter(file)) {
//            Vypsání aktuální konfigurace
            pw.println(config);

            for(Planet planet : stationGenerator.getPlanets()){
                pw.println(planet.export());
            }

            for(Station station : stationGenerator.getStations()){
                pw.println(station.export());
            }

            for(Planet planet : stationGenerator.getPlanets()){
                pw.println(planet.exportNeighbours());
            }

            for(Station station : stationGenerator.getStations()){
                pw.println(station.exportNeighbours());
            }
        } catch (FileNotFoundException e) {
            return false;
        }

        return true;
    }

    /**
     * Metoda sestaví objekt ze souboru
     *
     * @param file Reference na načítaný soubor
     */
    @Override
    public boolean restore(File file) {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line = br.readLine();
            config.updateConfig(line);

            List<Planet> planets = new ArrayList<>();
            for (int i = 0; i < config.getPlanetCount(); i++) {
                line = br.readLine();

                planets.add(Planet.restorePlanet(line));
            }
            planetGenerator.getPlanets().clear();
            planetGenerator.getPlanets().addAll(planets);

            System.out.println("načteno planets" + planets.size());

            List<Station> stations = new ArrayList<>();
            for (int i = 0; i < config.getStationCount(); i++) {
                line = br.readLine();
                stations.add(Station.restoreStation(line));
            }
            planetGenerator.getStations().clear();
            planetGenerator.getStations().addAll(stations);



            List<Path> paths = new ArrayList<>();

            for (int i = 0; i < ((config.getPlanetCount()) + config.getStationCount()); i++) {
                line = br.readLine();

                String data[] = line.split(":");

                int source = Integer.parseInt(data[0]);

                String neighbours[] = data[1].split(",");

                int intNeighbours[] = new int[neighbours.length];

                for (int j = 0; j < neighbours.length; j++) {
                    intNeighbours[j] = Integer.parseInt(neighbours[j]);
                }

                BasePlanet a;

                if(source >= 0){
                    a = planets.get(source);
                }
                else{
                    a = stations.get(Math.abs(source) - 1);
                }

                for(int neighbour : intNeighbours){
                    BasePlanet b;

                    if(neighbour >= 0){
                        b = planets.get(neighbour);
                    }
                    else{
                        b = stations.get(Math.abs(neighbour) - 1);
                    }


                    if(!a.isFull() && !b.isFull()){
                    paths.add(new Path(a, b));}

                }
            }

            planetGenerator.getPaths().clear();
            planetGenerator.getPaths().addAll(paths);

            Headquarters.getInstance().runDijkstra();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Metoda získá referenci na list stanic
     *
     * @return Referenci na list stanic
     */
    public ObservableList<Station> getStations() {
        return stations;
    }

    /**
     * Metoda získá referenci na list planet
     *
     * @return Referenci na list planet
     */
    public ObservableList<Planet> getPlanets() {
        return planets;
    }

    /**
     * Metoda získá referenci na list cest
     *
     * @return Referenci na list cest
     */
    public ObservableList<Path> getPaths() {
        return paths;
    }
}
