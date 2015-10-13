package cz.vrbik.pt.semestralka.model.galaxy.generator;

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
     * Vygeneruje novou planetu se zadaným minimálním prázdným okolím
     *
     * @param spacing Velikost okolí, ve kterém se nesmí nic nacházet
     * @return Novou referenci na planetu
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
