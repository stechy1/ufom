package cz.vrbik.pt.semestralka.model.galaxy.generator;

import cz.vrbik.pt.semestralka.model.galaxy.Galaxy;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Station;

/**
 * Generátor stanic
 */
public class StationGenerator extends GalaxyGenerator {

    /**
     * Konstruktor třídy {@link GalaxyGenerator}
     */
    public StationGenerator() {}

    /**
     * Konstruktor třídy {@link GalaxyGenerator}
     *
     * @param w Šířka galaxie využitelná generátorem
     * @param h Výška galaxie využitelná generátorem
     */
    public StationGenerator(int w, int h) {
        super(w, h);
    }

    /**
     * Konstruktor třídy {@link GalaxyGenerator}
     *
     * @param w      Šířka galaxie využitelná generátorem
     * @param h      Výška galaxie využitelná generátorem
     * @param galaxy Reference na galaxii
     */
    public StationGenerator(int w, int h, Galaxy galaxy) {
        super(w, h, galaxy);
    }

    /**
     * Vygeneruje objekty galaxie
     *
     * @param count   Počet objektů
     * @param spacing Velikost prázdného okolního prostoru kolem každého objektu
     */
    @Override
    public void generate(int count, int spacing) {
        for (int i = 0; i < count; i++) {
            Station s = generateStation(spacing);
            stations.add(s);
        }
    }

    private Station generateStation(int spacing) {
        int x,y;

        do {
            x = r.nextInt(width);
            y = r.nextInt(height);
        } while(isOccupied(x, y));

        markArea(x, y, spacing);
        return new Station(x, y);
    }
}
