package cz.vrbik.shippingsystem.planet;

import cz.vrbik.shippingsystem.SmallTransportShip;
import cz.vrbik.shippingsystem.ship.IShip;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Třída představující zásobovací stanici
 */
public class Station extends BasePlanet {

    private final Stack<IShip> parkedShips = new Stack<>();

    public void sendShipToTrip(List<BasePlanet> road) {
        IShip ship;

        ship = (parkedShips.size() != 0) ? parkedShips.pop() : new SmallTransportShip(this);

        ship.schedule(road);
        ship.startTrip();
    }

    /**
     * Aktualizační metoda objektu
     *
     * @param timestap Doba od počátku. Počátek = prvotní spuštění výpočtů galaxie
     */
    @Override
    public void update(int timestap) {

        dockedShips.forEach(parkedShips::push);
        dockedShips.clear();

        super.update(timestap);
    }

    @Override
    public String toString() {
        return "Station{}";
    }
}
