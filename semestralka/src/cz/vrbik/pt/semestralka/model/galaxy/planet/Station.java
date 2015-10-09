package cz.vrbik.pt.semestralka.model.galaxy.planet;

import cz.vrbik.pt.semestralka.Headquarters;
import cz.vrbik.pt.semestralka.model.galaxy.ship.IShip;
import cz.vrbik.pt.semestralka.model.galaxy.ship.SmallTransportShip;
import cz.vrbik.pt.semestralka.model.service.ResourceRequest;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Stack;

/**
 * Třída představující zásobovací stanici
 */
public class Station extends BasePlanet {

    private static final Logger log = Logger.getLogger(Station.class.getName());

    public static final int DEFAULT_WIDTH = 8;
    public static final int DEFAULT_HEIGHT = 8;

    private final Stack<IShip> parkedShips = new Stack<>();

    private static int ID_COUNTER = -1;
    private static int MAX_SHIP_OUT = 250;

    private int shipOut = 0;

    /**
     * Vyrestartuje čítač stanic
     */
    public static void resetCounter() {
        ID_COUNTER = -1;
    }


    /**
     * Statická metoda pro vrácení objektu Station z konfiguračního řádku
     * @param line konfigurační řádek
     * @return instance rekonstruované planety
     */
    public static Station restoreStation(String line) {
        String[] data = line.split(";");
        return new Station(Double.parseDouble(data[0]), Double.parseDouble(data[1]), DEFAULT_WIDTH, DEFAULT_HEIGHT, Integer.parseInt(data[2]), data[3]);
    }

    /**
     * Konstruktor třídy {@link BasePlanet} s výchozí výškou a šířkou objektu
     *
     * @param x Vodorovná souřadnice stanice
     * @param y Svislá souřadnice stanice
     */
    public Station(double x, double y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Konstruktor třídy {@link BasePlanet}
     *
     * @param x Vodorovná souřadnice stanice
     * @param y Svislá souřadnice stanice
     * @param width Šířka stanice
     * @param height Výška stanice
     */
    public Station(double x, double y, double width, double height) {
        this(x, y, width, height, ID_COUNTER, "MS_" + ID_COUNTER--);
    }

    /**
     * Konstruktor třídy {@link BasePlanet}
     *
     * @param x Vodorovná souřadnice stanice
     * @param y Svislá souřadnice stanice
     * @param width Šířka stanice
     * @param height Výška stanice
     * @param id Id stanice
     * @param name Název stanice
     */
    public Station(double x, double y, double width, double height, int id, String name) {
        super(x, y, width, height);
        this.id = id;
        this.name = name;
    }

    /**
     * Vyšle loď na cestu
     *
     * @param road Spojový seznam představující jednu cestu
     */
    public boolean sendShipToTrip(List<BasePlanet> road, ResourceRequest request) {
        if (shipOut == MAX_SHIP_OUT)
            return false;

        shipOut++;
        IShip ship;

        ship = (parkedShips.size() != 0) ? parkedShips.pop() : new SmallTransportShip(this);
        ship.setRequest(request);
        ship.schedule(road);
        emptyShips.add(ship);

        log.debug("Připravuji loď " + ship + " na cestu");
        //ship.startTrip();
        return true;
    }
    
    public String export(){
        return x + ";" + y + ";" + id + ";" + name;
    }

    /**
     * Metoda aktualizující logiku objektu
     *
     * @param timestamp Doba od spuštění simulace
     */
    @Override
    public void update(int timestamp) {
        shipsReadyToGo.forEach(IShip::startTrip);
        shipsReadyToGo.clear();

        emptyShips.stream().filter(IShip::isReady).forEach(emptyShip -> {
            emptyShip.loadCargo(emptyShip.getRequest().quantity);
            shipsReadyToGo.add(emptyShip);

        });
        emptyShips.removeAll(shipsReadyToGo);

        for (IShip dockedShip : dockedShips) {
            shipOut--;
            parkedShips.push(dockedShip);
            if (dockedShip.isHijacked()){
                dockedShip.setHijacked(false);
                //TODO čištění lodi po příjezdu
            }
        }

        dockedShips.clear();


    }

    @Override
    public void render(GraphicsContext g) {
        g.setFill(Color.LIME);
        super.render(g);
    }
}
