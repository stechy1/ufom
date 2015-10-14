package cz.vrbik.pt.semestralka.model.galaxy.planet;

import cz.vrbik.pt.semestralka.Headquarters;
import cz.vrbik.pt.semestralka.model.galaxy.generator.GalaxyGenerator;
import cz.vrbik.pt.semestralka.model.galaxy.ship.IShip;
import cz.vrbik.pt.semestralka.model.service.PlanetNames;
import cz.vrbik.pt.semestralka.model.service.RequestPriority;
import cz.vrbik.pt.semestralka.model.service.ResourceRequest;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Třída představující jednu planetu v galaxii
 */
public class Planet extends BasePlanet {

    private static final Logger log = Logger.getLogger(Planet.class.getName());

    public static final int DEFAULT_WIDTH = 2;
    public static final int DEFAULT_HEIGHT = 2;
    public static final int INTERESTING_LIMIT = 40000;

    private static int ID_COUNTER = 0;
    private static final int MAX_POPULATION = 10000000;
    private static final int MIN_POPULATION = 100000;
    private static final PlanetNames names = PlanetNames.getInstance();

    public List<Integer> inhabitantStatistics = new ArrayList<>();
    public List<Integer> deliversStatistics = new ArrayList<>();

    private int lastDeliver = 0;
    public int inhabitants;
    public int graves = 0;
    public int inhabitantsEndagered = 0;

    /**
     * Vyrestartuje počítadlo planet
     */
    public static void resetCounter() {
        ID_COUNTER = 0;
    }


    public static Planet restorePlanet(String line) {
        String[] data = line.split(";");
        return new Planet(Double.parseDouble(data[0]), Double.parseDouble(data[1]), DEFAULT_WIDTH, DEFAULT_HEIGHT,
                Integer.parseInt(data[2]), data[3], Integer.parseInt(data[4]));
    }

    /**
     * Konstruktor třídy {@link BasePlanet} s výchozí výškou a šířkou objektu
     *
     * @param x      Vodorovná souřadnice planety
     * @param y      Svislá souřadnice planety
     */
    public Planet(double x, double y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Konstruktor třídy {@link BasePlanet}
     *
     * @param x      Vodorovná souřadnice planety
     * @param y      Svislá souřadnice planety
     * @param width  Šířka objektu
     * @param height Výška objektu
     */
    public Planet(double x, double y, double width, double height) {
        this(x, y, width, height, ID_COUNTER++, names.getName(), generateInhabitants());
    }

    /**
     * Konstruktor třídy {@link BasePlanet}
     *
     * @param x           Vodorovná souřadnice planety
     * @param y           Svislá souřadnice planety
     * @param width       Šířka planety
     * @param height      Výška planety
     * @param id          Id planety
     * @param name        Název planety
     * @param inhabitants Počet obyvatel
     */
    public Planet(double x, double y, double width, double height, int id, String name, int inhabitants) {
        super(x, y, width, height);
        this.id = id;
        this.name = name;
        this.inhabitants = inhabitants;
    }

    /**
     * Metoda pro generování počtu původních obyvatel
     */
    private static int generateInhabitants() {
        return GalaxyGenerator.r.nextInt((MAX_POPULATION - MIN_POPULATION) + 1) + MIN_POPULATION;
    }

    /**
     * Metoda pro vygenorování vlastních zdrojů pro výrobu léků
     *
     * @return počet obyvatel o které se planeta umí postarat
     */
    private int generateProduction() {
        return GalaxyGenerator.r.nextInt((60) + 1) + 20;
    }

    /**
     * Metoda pro zjištění, jestli je planeta pro společnost ještě důležitá
     *
     * @return True, pokud je planeta jestě důležitá, jinak false
     */
    public boolean isNotInteresting() {
        return (inhabitants < INTERESTING_LIMIT);
    }

    /**
     * Metoda pro vrácení konfiguračního řádku reprezentující planetu
     * @return popis planety
     */
    public String export(){
        return x + ";" + y + ";" + id + ";" + name + ";" + inhabitants;
    }

    public void makeManualRequest() {
        Headquarters.getInstance().makeRequest(new ResourceRequest(this, inhabitantsEndagered, RequestPriority.HIGH));
    }

    /**
     * Metoda vykreslující objekt
     *
     * @param g Reference na grafický kontext
     */
    @Override
    public void render(GraphicsContext g) {
        if(isNotInteresting()){
            g.setFill(Color.BLACK);
        }
        else{
            g.setFill(Color.AQUA);
        }
        super.render(g);
    }

    /**
     * Pošle na velitelství požadavek, že chce nové zásoby
     *
     * @param amount Množství léků
     */
    public void sendRequest(int amount){
        Headquarters.getInstance().makeRequest(new ResourceRequest(this, amount));
    }

    /**
     * Metoda pro zapsání statistiky za uplynulý měsíc pro konkrétní planetu
     */
    private void monthStatistics(){
        inhabitantStatistics.add(inhabitants);
        deliversStatistics.add(lastDeliver);

        lastDeliver = 0;
    }


    /**
     * Metoda pro aktualizaci stavu planety
     *
     * @param timestamp Doba od spuštění simulace
     */
    @Override
    public void update(int timestamp) {

        if(timestamp % (30*25) == 0) {

            if(inhabitantsEndagered > 0){
                graves += inhabitantsEndagered;
            }
            inhabitants = (inhabitants - inhabitantsEndagered);
            inhabitantsEndagered = 0;


            int production = generateProduction();
            inhabitantsEndagered = inhabitants - ((inhabitants/100) * production);
            sendRequest(inhabitantsEndagered);

            monthStatistics();
        }

        shipsReadyToGo.forEach(IShip::continueTrip);
        shipsReadyToGo.clear();

        for (IShip emptyShip : emptyShips) {
            emptyShip.schedule();
            emptyShip.startTrip();
        }
        emptyShips.clear();

        dockedShips.stream().filter(IShip::isReady).forEach(iShip -> {
            inhabitantsEndagered = (inhabitantsEndagered - iShip.getCargo());
            lastDeliver += iShip.getCargo();

            log.debug("na planetu " + this.getName() + " dorazily zásoby: " + iShip.getCargo() + ", jeste zbyva dovezt: " + inhabitantsEndagered );

            iShip.unLoadCargo(iShip.getCargo());
            emptyShips.add(iShip);
        });

        dockedShips.removeAll(emptyShips);
    }

    @Override
    public String toString() {
        return String.format("%s; Inhabitants: %d", super.toString(), inhabitants);
    }
}
