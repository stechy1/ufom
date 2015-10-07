package cz.vrbik.pt.semestralka.model.galaxy.planet;

import cz.vrbik.pt.semestralka.model.galaxy.ship.IShip;

/**
 * Rozhraní pro správu lodí na planetě
 */
public interface IShipControll {

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