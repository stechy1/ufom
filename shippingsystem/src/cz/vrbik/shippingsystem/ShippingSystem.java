package cz.vrbik.shippingsystem;

import cz.vrbik.shippingsystem.planet.BasePlanet;
import cz.vrbik.shippingsystem.planet.Planet;
import cz.vrbik.shippingsystem.planet.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Spouštěcí třída pro testování letového systému
 */
public final class ShippingSystem {

    static final List<BasePlanet> planetList = new ArrayList<>();

    public static void main(String[] args) {
        Station station = new Station();

        Planet planet1 = new Planet(50000);
        Planet planet2 = new Planet(160000);
        Planet planet3 = new Planet(25000);
        Planet planet4 = new Planet(98637);

        planetList.addAll(Arrays.asList(station, planet1, planet2, planet3, planet4));

        station.connections.addAll(Arrays.asList(new Connection(planet1, 10), new Connection(planet2, 1)));
        planet1.connections.addAll(Arrays.asList(new Connection(station, 10), new Connection(planet2, 3)));
        planet2.connections.addAll(Arrays.asList(new Connection(station, 1),  new Connection(planet1, 3), new Connection(planet3, 6)));
        planet3.connections.addAll(Arrays.asList(new Connection(planet2, 6), new Connection(planet4, 9)));
        planet4.connections.addAll(Arrays.asList(new Connection(planet3, 9)));

        //station.sendShipToTrip(Arrays.asList(planet1, planet2, planet3, planet4));
        station.sendShipToTrip(Arrays.asList(planet2, planet1));

        for (int i = 0; i < 90; i++) {
            //System.out.println("Aktualizace číslo: " + i);
            for (BasePlanet planet : planetList) {
                planet.update(i);
            }
        }
    }
}
