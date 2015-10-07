package cz.vrbik.shippingsystem.ship;

import cz.vrbik.shippingsystem.planet.BasePlanet;

import java.util.List;

/**
 * Rozhraní pro kontrolu stavu cesty
 */
public interface ITripControllable {

    /**
     * Odstartuje loď na cestu
     */
    void startTrip();

    /**
     * Pošle loď na další cestu
     */
    void continueTrip();

    /**
     * Metoda zjišťujě, zda-li je loď na konci své cesty
     * @return Vrátí true, pokud už dál loď nepoletí, jinak false
     */
    boolean isEndOfTrip();

    /**
     * Metoda zjišťuje, zda-li má loď vystoupit z hyperprostoru
     * @return True, pokud loď dosáhla dočasného cíle, jinak false
     */
    boolean isEndOfConnection();

    /**
     * Inkrementuje celkový postup lodě
     */
    void incrementTotalProgress();

    /**
     * Inkrementuje postup lodi na aktuálním meziplanetárním spojení
     */
    void incrementConnectionProgress();

    /**
     * Vrátí list s naplánovanou cestou
     */
    List<BasePlanet> getTrip();
}
