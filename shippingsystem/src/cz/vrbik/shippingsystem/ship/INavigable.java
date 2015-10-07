package cz.vrbik.shippingsystem.ship;

import cz.vrbik.shippingsystem.planet.BasePlanet;

/**
 * Rozhraní pro určení cílů lodě
 */
public interface INavigable {

    /**
     * Vrátí referenci na další cíl
     * @return
     */
    BasePlanet getNextDestination();

    /**
     * Vrátí referenci na poslední cílovou planetu
     * @return
     */
    BasePlanet getTotalDestination();
}
