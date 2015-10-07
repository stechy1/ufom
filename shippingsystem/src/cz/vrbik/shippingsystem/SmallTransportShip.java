package cz.vrbik.shippingsystem;


import cz.vrbik.shippingsystem.planet.BasePlanet;
import cz.vrbik.shippingsystem.ship.BaseShip;
import cz.vrbik.shippingsystem.ship.ILoadable;

/**
 * Základní třída pro loď
 */
public class SmallTransportShip extends BaseShip implements ILoadable {

    private int capacity = 50000;
    private int cargo = 0;

    /**
     * Vytvoří novou loď s mateřskou planetou/stanicí
     * @param homePlanet Mateřská planeta/stanice
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
    }

    /**
     * Vyloží dané množství
     */
    @Override
    public int unLoadCargo() {
        int toReturn = cargo;
        cargo = 0;
        return toReturn;
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

    @Override
    public String toString() {
        return "SmallTransportShip{}";
    }
}
