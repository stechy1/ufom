package cz.vrbik.shippingsystem.planet;

/**
 * Třída představující koncovou planetu
 */
public class Planet extends BasePlanet {

    private int inhabitants;


    public Planet(int inhabitants) {
        this.inhabitants = inhabitants;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "inhabitants=" + inhabitants +
                '}';
    }
}
