package cz.vrbik.ufo.model;

import cz.vrbik.ufo.model.galaxyobject.BasePlanet;
import cz.vrbik.ufo.model.galaxyobject.Ship;

/**
 * Třída představující spojení mezi dvěma planetárními objekty
 */
public final class Connection {

    public final BasePlanet endPoint;
    public final int dangerous;

    private Ship ship;

    /**
     * Vytvoří nové spojení mezi planetou a jejím sousedem
     * @param endPoint Sousední planeta
     * @param dangerous Nebezpečí cesty
     */
    public Connection(BasePlanet endPoint, int dangerous) {
        this.endPoint = endPoint;
        this.dangerous = dangerous;
    }

    public void sendShipToEndpoint(Ship ship) {
        this.ship = ship;
    }
}
