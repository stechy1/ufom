package cz.vrbik.pt.semestralka.model.service;

import cz.vrbik.pt.semestralka.model.galaxy.planet.Planet;

/**
 * Třída představuje jeden požadavek planety
 */
public final class ResourceRequest {

    public final Planet requestPlanet;
    public int quantity;
    public RequestPriority priority;

    /**
     * Konstruktor třídy {@link ResourceRequest}
     *
     * @param requestPlanet Reference na planetu, která vytvořila request
     * @param quantity      Počet léků, které planeta chce
     */
    public ResourceRequest(Planet requestPlanet, int quantity) {
        this(requestPlanet, quantity, RequestPriority.NORMAL);
    }

    /**
     * Konstruktor třídy {@link ResourceRequest}
     *
     * @param requestPlanet Reference na planetu, která vytvořila request
     * @param quantity      Počet léků, které planeta chce
     * @param priority      Priorita requestu
     */
    public ResourceRequest(Planet requestPlanet, int quantity, RequestPriority priority) {
        this.requestPlanet = requestPlanet;
        this.quantity = quantity;
        this.priority = priority;
    }
}
