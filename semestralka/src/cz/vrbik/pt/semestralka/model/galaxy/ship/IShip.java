package cz.vrbik.pt.semestralka.model.galaxy.ship;

import cz.vrbik.pt.semestralka.model.galaxy.IGraphicalObject;
import cz.vrbik.pt.semestralka.model.service.ResourceRequest;

/**
 * Značkovací rozhraní pro všechny lodě
 */
public interface IShip extends
        ITripControllable, INavigable, ISchedulable, ILoadable, IHijackable, IPirateControl, IGraphicalObject {

    /**
     * Metoda zjistí, zda-li je loď připravena k odletu
     *
     * @return True, pokud je loď připravena k odletu, jinak false
     */
    boolean isReady();

    void setRequest(ResourceRequest request);

    ResourceRequest getRequest();

}
