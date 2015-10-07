package cz.vrbik.pt.semestralka.model.service;

import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;

import java.util.List;

/**
 * Třída reprezentuje jednoduchou strukturu pro pana dijkstra
 */
public final class Prepravka {

    public final BasePlanet source;
    public final List<BasePlanet> path;
    public final double weight;
    private boolean nevhodna = false;

    public Prepravka(BasePlanet source, List<BasePlanet> path, double weight) {
        this.source = source;
        this.path = path;
        this.weight = weight;
    }

    /**
     * Zjistí, zda-li je cesta nevhodná
     *
     * @return True, pokud je cesta nevhodná, jinak false
     */
    public boolean isNevhodna() {
        return nevhodna;
    }

    /**
     * Nastaví, zda-li je cesta nevhodná
     *
     * @param nevhodna True, pokud je cesta nevhodná, jinak false
     */
    public void setNevhodna(boolean nevhodna) {
        this.nevhodna = nevhodna;
    }
}
