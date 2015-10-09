package cz.vrbik.pt.semestralka.model.service;

import com.sun.istack.internal.NotNull;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Planet;

/**
 * Třída představuje jeden požadavek planety
 */
public final class ResourceRequest {

    public final Planet requestPlanet;
    public int quantity;

    /**
     * Konstruktor třídy {@link ResourceRequest}
     *
     * @param requestPlanet Reference na planetu, která vytvořila request
     * @param quantity      Počet léků, které planeta chce
     */
    public ResourceRequest(Planet requestPlanet, int quantity) {
        this.requestPlanet = requestPlanet;
        this.quantity = quantity;
    }

}
