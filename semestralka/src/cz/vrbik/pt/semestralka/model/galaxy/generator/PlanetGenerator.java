package cz.vrbik.pt.semestralka.model.galaxy.generator;

import cz.vrbik.pt.semestralka.model.galaxy.Galaxy;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Planet;

/**
 * Generátor planet
 */
public class PlanetGenerator extends GalaxyGenerator {

    /**
     * Konstruktor třídy {@link GalaxyGenerator}
     */
    public PlanetGenerator() {}

    /**
     * Konstruktor třídy {@link GalaxyGenerator}
     *
     * @param w Šířka galaxie využitelná generátorem
     * @param h Výška galaxie využitelná generátorem
     */
    public PlanetGenerator(int w, int h) {
        super(w, h);
    }

    /**
     * Konstruktor třídy {@link GalaxyGenerator}
     *
     * @param w      Šířka galaxie využitelná generátorem
     * @param h      Výška galaxie využitelná generátorem
     * @param galaxy Reference na galaxii
     */
    public PlanetGenerator(int w, int h, Galaxy galaxy) {
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
            Planet p = generatePlanet(spacing);
            planets.add(p);
        }

        neighboursFinder();
    }

    /**
     *
     * @param spacing
     * @return
     */
    private Planet generatePlanet(int spacing) {

        int x,y;
        do {
            x = r.nextInt(width);
            y = r.nextInt(height);

        } while(isOccupied(x, y));

        markArea(x, y, spacing);
        return new Planet(x, y);
    }
}
