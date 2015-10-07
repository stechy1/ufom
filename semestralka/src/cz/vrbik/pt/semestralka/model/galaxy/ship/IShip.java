package cz.vrbik.pt.semestralka.model.galaxy.ship;

/**
 * Značkovací rozhraní pro všechny lodě
 */
public interface IShip extends
        ITripControllable, INavigable, ISchedulable, ILoadable, IHijackable, IPirateControl {}