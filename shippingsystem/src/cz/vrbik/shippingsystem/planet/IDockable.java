package cz.vrbik.shippingsystem.planet;

import cz.vrbik.shippingsystem.ship.IShip;

/**
 * Rozhraní pro dokování lodí
 */
public interface IDockable {

    /**
     * Nadokuje loď
     * @param ship
     */
    void dock(IShip ship);

    /**
     * Vyšle loď správným smerem
     * @param ship
     */
    void send(IShip ship);
}
