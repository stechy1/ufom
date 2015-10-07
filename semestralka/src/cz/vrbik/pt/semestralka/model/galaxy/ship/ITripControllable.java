package cz.vrbik.pt.semestralka.model.galaxy.ship;


import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;

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
     * Pošle loď z aktuální pozice zpět domů
     */
    void turnBackToHome();

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
     * Dekrementuje postup lodi na aktuálním meziplanetárním spojení
     */
    void decrementConnectionProgress();

    /**
     * Nastaví celkovou dobu na jedné cestě
     *
     * @param value Doba na cestě
     */
    void setConnectionProgress(int value);

    /**
     * Vrátí list s naplánovanou cestou
     */
    List<BasePlanet> getTrip();
}
