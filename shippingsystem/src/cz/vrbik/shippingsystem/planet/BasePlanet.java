package cz.vrbik.shippingsystem.planet;

import cz.vrbik.shippingsystem.Connection;
import cz.vrbik.shippingsystem.IUpdatable;
import cz.vrbik.shippingsystem.ship.IShip;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Výchozí třída pro planetu
 */
public abstract class BasePlanet implements IUpdatable, IDockable {

    /*public final double x;
    public final double y;
    public final int id;
    public final String name;*/
    public final List<Connection> connections = new ArrayList<>(5);
    public final List<IShip> dockedShips = new ArrayList<>();
    private final List<IShip> shipsReadyToGo = new ArrayList<>();
    private final List<IShip> emptyShips = new ArrayList<>();

    /**
     * Nadokuje loď
     *
     * @param ship
     */
    @Override
    public void dock(IShip ship) {
        if (ship.isEndOfTrip()) {
            dockedShips.add(ship);
            System.out.println("Parkuji loď: " + ship + " na planetě: " + this);
        }
        else {
            shipsReadyToGo.add(ship);
        }
    }

    /**
     * Vyšle loď správným smerem
     *
     * @param ship
     */
    @Override
    public void send(IShip ship) {
        BasePlanet endPoint = ship.getNextDestination();
        getConnection(endPoint).sendShip(ship);
    }

    /**
     * Vrátí spojení na základě koncového bodu
     *
     * @param endPoint Koncový bod
     * @return Spojení ke koncovému bodu
     */
    public Connection getConnection(BasePlanet endPoint) {
        for (Connection connection : connections)
            if (connection.endPoint == endPoint)
                return connection;

        return null;
    }

    /**
     * Aktualizační metoda objektu
     *
     * @param timestap Doba od počátku. Počátek = prvotní spuštění výpočtů galaxie
     */
    @Override
    public void update(int timestap) {
        for (Connection connection : connections) {
            connection.update(timestap);
        }

        shipsReadyToGo.forEach(IShip::continueTrip);
        shipsReadyToGo.clear();

        for (IShip emptyShip : emptyShips) {
            emptyShip.schedule();
            emptyShip.startTrip();
        }

        emptyShips.clear();
        emptyShips.addAll(dockedShips.stream().collect(Collectors.toList()));

        dockedShips.removeAll(emptyShips);
    }

    @Override
    public String toString() {
        return "BasePlanet{}";
    }
}
