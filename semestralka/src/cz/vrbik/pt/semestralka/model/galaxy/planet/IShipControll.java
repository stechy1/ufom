package cz.vrbik.pt.semestralka.model.galaxy.planet;

import cz.vrbik.pt.semestralka.model.galaxy.ship.IShip;

/**
 * Rozhraní pro správu lodí na planetě
 */
public interface IShipControll {

    /**
     * Nadokuje loď
     * @param ship Reference na loď, která se má nadokovat
     */
    void dock(IShip ship);

    /**
     * Vyšle loď správným smerem
     * @param ship Reference na loď, kterou má poslat
     */
    void send(IShip ship);
}