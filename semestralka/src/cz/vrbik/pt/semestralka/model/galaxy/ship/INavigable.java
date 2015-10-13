package cz.vrbik.pt.semestralka.model.galaxy.ship;


import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;

/**
 * Rozhraní pro určení cílů lodě
 */
public interface INavigable {

    /**
     *@return Referenci na další cíl
     */
    BasePlanet getNextDestination();

    /**
     * @return Referenci na poslední cílovou planetu
     */
    BasePlanet getTotalDestination();
}
