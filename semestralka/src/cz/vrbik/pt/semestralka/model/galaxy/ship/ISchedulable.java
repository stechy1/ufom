package cz.vrbik.pt.semestralka.model.galaxy.ship;


import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;

import java.util.List;

/**
 * Rozhraní pro naplánování cesty
 */
public interface ISchedulable {

    /**
     * Naplánuje zpětnou cestu
     * Vezme tu aktuální, invertuje jí a jede
     */
    void schedule();

    /**
     * Naplánuje novou cestu
     *
     * @param road Reference na list cest k cíli
     */
    void schedule(List<BasePlanet> road);

}
