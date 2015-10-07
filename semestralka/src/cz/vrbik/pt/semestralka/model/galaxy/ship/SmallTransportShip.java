package cz.vrbik.pt.semestralka.model.galaxy.ship;

import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;
import org.apache.log4j.Logger;

/**
 * Třída představuje malou transportní loď
 */
public class SmallTransportShip extends BaseShip implements ILoadable {

    private static final Logger log = Logger.getLogger(SmallTransportShip.class.getName());

    private int capacity = 50000;
    private int cargo = 0;

    /**
     * Konstruktor třídy {@link BaseShip}
     *
     * Vytvoří novou vesmírnou loď
     *
     * @param homePlanet Reference na domovskou planetu lodi
     */
    public SmallTransportShip(BasePlanet homePlanet) {
        super(homePlanet);
    }

    /**
     * Naloží dané množství
     *
     * @param mount množství
     */
    @Override
    public void loadCargo(int mount) {
        cargo += mount;
        log.debug(String.format("Plním loď množstvím: %d", mount));
        //System.out.println("Plním loď množstvím: " + mount);
    }

    /**
     * Vyloží veškerý náklad
     */
    @Override
    public boolean unLoadCargo(int mount) {
        if (mount > cargo)
            return false;

        cargo -= mount;
        log.debug(String.format("Vyprazdňuji loď množstvím: %d", mount));
        //System.out.println("Vyprazdňuji loď množstvím: " + mount);
        return true;
    }

    /**
     * Metoda získá aktuální naložené množství
     *
     * @return Aktuální naložené mnozství
     */
    @Override
    public int getCargo() {
        return cargo;
    }

    /**
     * Vrátí množství, které loď může naložit
     *
     * @return naložitelné množství
     */
    @Override
    public int getCapacity() {
        return capacity;
    }

    /**
     * Metoda zjišťuje, zda-li je loď plně naložena
     *
     * @return True, pokud je loď plně naložena, jinak false
     */
    @Override
    public boolean isShipFullyLoaded() {
        return cargo == capacity;
    }

    @Override
    public String toString() {
        return String.format("SmallTransportShip{id: %d, capacity:%d, cargo:%d}", id, capacity, cargo);
    }
}
