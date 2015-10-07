package cz.vrbik.shippingsystem.ship;

import cz.vrbik.shippingsystem.planet.BasePlanet;

import java.util.List;

/**
 * Rozhraní pro naplánování cesty
 */
public interface ISchedulable {

    void schedule();

    void schedule(List<BasePlanet> road);

}
