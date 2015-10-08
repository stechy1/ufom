package cz.vrbik.pt.semestralka.model.galaxy.ship;

/**
 * Značkovací rozhraní pro všechny lodě
 */
public interface IShip extends
        ITripControllable, INavigable, ISchedulable, ILoadable, IHijackable, IPirateControl {

    /**
     * Metoda zjistí, zda-li je loď připravena k odletu
     *
     * @return True, pokud je loď připravena k odletu, jinak false
     */
    boolean isReady();

}
