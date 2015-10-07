package cz.vrbik.shippingsystem;

import cz.vrbik.shippingsystem.planet.BasePlanet;
import cz.vrbik.shippingsystem.ship.IShip;

import java.util.ArrayList;
import java.util.List;

/**
 * Třída představující spojení mezi dvěma body
 */
public final class Connection implements IUpdatable {

    public final BasePlanet endPoint;
    public final List<IShip> ships = new ArrayList<>();
    private final List<IShip> shipsToAdd = new ArrayList<>();
    private final List<IShip> shipsToRemove = new ArrayList<>();
    public final int dangerous;

    /**
     * Vytvoří nové spojení s planetou
     * @param endPoint Koncový bod spojení
     * @param dangerous Nebezpečí cesty
     */
    public Connection(BasePlanet endPoint, int dangerous) {
        this.endPoint = endPoint;
        this.dangerous = dangerous;
    }

    /**
     * Vyšle loď na cestu
     * @param ship Reference na loď
     */
    public void sendShip(IShip ship) {
        shipsToAdd.add(ship);
        //System.out.println("Posílám loď " + ship + " do: " + endPoint);
    }

    public void removeShip(IShip ship) {
        shipsToRemove.add(ship);
    }

    @Override
    public void update(int timestap) {

        ships.addAll(shipsToAdd);
        ships.removeAll(shipsToRemove);

        shipsToAdd.clear();
        shipsToRemove.clear();

        for (IShip ship : ships) {
            ship.incrementConnectionProgress();
            if (ship.isEndOfConnection()) {
                //System.out.println("Odebírám loď " + ship + "ze spojení: " + this);
                removeShip(ship);
            }
        }

        ships.addAll(shipsToAdd);
        ships.removeAll(shipsToRemove);

        shipsToAdd.clear();
        shipsToRemove.clear();
    }

    @Override
    public String toString() {
        return "Connection{" +
                "endPoint=" + endPoint +
                '}';
    }
}
