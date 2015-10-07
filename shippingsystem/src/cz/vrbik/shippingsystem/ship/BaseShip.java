package cz.vrbik.shippingsystem.ship;

import cz.vrbik.shippingsystem.planet.BasePlanet;

import java.util.*;

/**
 * Výchozí třida pro každou loď
 */
public abstract class BaseShip implements IShip {

    public final LinkedList<BasePlanet> trip = new LinkedList<>();

    private Iterator<BasePlanet> tripIterator;
    protected BasePlanet homePlanet;
    protected BasePlanet actualPlanet;
    protected BasePlanet nextPlanet;

    protected boolean endConnection = false;
    protected int totalProgress = 0;
    protected int connectionProgress = 0;

    /**
     * Vytvoří loď s domovskou planetou
     *
     * @param homePlanet Domovská planeta lodi.
     */
    public BaseShip(BasePlanet homePlanet) {
        this.homePlanet = homePlanet;
        actualPlanet = homePlanet;
    }

    /**
     * Odstartuje loď na cestu
     */
    @Override
    public void startTrip() {
        endConnection = false;
        try {
            nextPlanet = tripIterator.next();
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
        }
        actualPlanet.send(this);
    }

    /**
     * Pošle loď na další planetu
     */
    @Override
    public void continueTrip() {
        if (nextPlanet != null)
            actualPlanet = nextPlanet;
        else
            actualPlanet = tripIterator.next();
        startTrip();
    }

    /**
     * Metoda zjišťujě, zda-li je loď na konci své cesty
     *
     * @return Vrátí true, pokud už dál loď nepoletí, jinak false
     */
    @Override
    public boolean isEndOfTrip() {
        return totalProgress == trip.size() - 1;
    }

    /**
     * Metoda zjišťuje, zda-li má loď vystoupit z hyperprostoru
     *
     * @return True, pokud loď dosáhla dočasného cíle, jinak false
     */
    @Override
    public boolean isEndOfConnection() {
        return endConnection;
    }

    /**
     * Inkrementuje celkový postup lodě
     */
    @Override
    public void incrementTotalProgress() {
        totalProgress++;
    }

    /**
     * Inkrementuje postup lodi na aktuálním meziplanetárním spojení
     */
    @Override
    public void incrementConnectionProgress() {
        connectionProgress++;
        if (connectionProgress == 2) {
            endConnection = true;
            connectionProgress = 0;
            incrementTotalProgress();
            nextPlanet.dock(this);
        }
    }

    /**
     * Naplánuje zpětnou cestu
     * Vezme tu aktuální, invertuje jí a jede
     */
    @Override
    public void schedule() {
        totalProgress = 0;
        Collections.reverse(trip);
        actualPlanet = nextPlanet;
        tripIterator = trip.iterator();
        tripIterator.next();
    }

    /**
     * Naplánuje novou cestu
     *
     * @param road Reference na list cest k cíli
     */
    @Override
    public void schedule(List<BasePlanet> road) {
        totalProgress = 0;
        trip.clear();
        trip.add(homePlanet);
        trip.addAll(road);
        tripIterator = trip.iterator();
        tripIterator.next();
    }

    /**
     * Vrátí referenci na další cíl
     *
     * @return Referenci na další planetu
     */
    @Override
    public BasePlanet getNextDestination() {
        return nextPlanet;
    }

    /**
     * Vrátí referenci na poslední cílovou planetu
     *
     * @return Referenci na cílovou planetu
     */
    @Override
    public BasePlanet getTotalDestination() {
        return trip.getLast();
    }

    /**
     * Vrátí list s naplánovanou cestou
     */
    @Override
    public List<BasePlanet> getTrip() {
        return trip;
    }
}
