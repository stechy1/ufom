package cz.vrbik.pt.semestralka.model.galaxy;

import cz.vrbik.pt.semestralka.model.IUpdatable;
import cz.vrbik.pt.semestralka.model.galaxy.generator.GalaxyGenerator;
import cz.vrbik.pt.semestralka.model.galaxy.planet.BasePlanet;
import cz.vrbik.pt.semestralka.model.galaxy.ship.IShip;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Třída představující cestu mezi dvěma planetami
 */
public final class Path implements IUpdatable {

    private static final Logger log = Logger.getLogger(Path.class.getName());

    private static int ID_COUNTER = 0;

    private final BasePlanet a;
    private final BasePlanet b;

    public final List<IShip> ships = new ArrayList<>();
    private final List<IShip> shipsToAdd = new ArrayList<>();
    private final List<IShip> shipsToRemove = new ArrayList<>();

    public final int dangerous;
    public final int id;
    public final double weight;
    private boolean visited = false;
    private int shipCounter = 0;

    private boolean pirateDetected = false;
    private boolean pirate;

    private double cost = 0;

    /**
     * Konstruktor třídy {@link Path}
     *
     * @param a Reference na první planetu
     * @param b Reference na druhou planetu
     */
    public Path(BasePlanet a, BasePlanet b) {
        id = ID_COUNTER++;

        this.a = a;
        this.b = b;

        a.addPath(this);
        b.addPath(this);

        weight = a.planetDistance(b);
        dangerous = 0;

        pirate = generatePirate();

    }

    /**
     * Metoda, která určuje zda-li je cesta obsazena piráty a hrozí tak přepadení
     * zároveň nastavuje detekování, zda li je cesta nebezpečná na false, tedy
     * to lodě musejí znovu zjistit
     *
     * @return True, pokud je cesta obsazena piráty o hrozí přepadení, jinak false
     */
    private boolean generatePirate() {
        pirateDetected = false;
        return GalaxyGenerator.r.nextInt(100) <= 20;
    }

    /**
     * Metoda pro zjištění zda proběhne útok
     *
     * @return True, pokud proběhne útok, jinak false
     */
    private boolean hijack() {

        return pirate && GalaxyGenerator.r.nextInt(100) <= 10;
    }

    /**
     * Metoda zjistí opačnou planetu spojení
     *
     * @param x Reference na planetu, ke které se hledá soused
     * @return Soused zadané planety
     */
    public BasePlanet getNeighbour(BasePlanet x) {
        if (x == a) {
            return b;
        } else if (x == b) {
            return a;
        }

        //nemělo by nikdy nastat
        return null;
    }

    /**
     * Přidá loď na tuto cestu
     *
     * @param ship Reference na loď
     */
    public void addShip(IShip ship) {
        shipsToAdd.add(ship);
        visited = !visited;
        //shipCounter++;
    }

    /**
     * Odebere loď z cesty
     *
     * @param ship Reference na odebranou loď
     */
    public void removeShip(IShip ship) {
        shipsToRemove.add(ship);
        //shipCounter--;
    }

    /**
     * Metoda aktualizující logiku objektu
     *
     * @param timestamp Doba od spuštění simulace
     */
    @Override
    public void update(int timestamp) {
        ships.addAll(shipsToAdd);
        ships.removeAll(shipsToRemove);

        shipsToAdd.clear();
        shipsToRemove.clear();

        for (IShip ship : ships) {
            ship.decrementConnectionProgress();
            if (ship.isEndOfConnection()) {
                //log.debug("Odebírám loď " + ship + "ze spojení: " + this);
                removeShip(ship);
            }

            if (ship.isChecked())
                continue;

            if (hijack() && ship.isShipFullyLoaded() && !ship.isChecked()) {
                ship.setChecked(false);
                pirateDetected = true;
                ship.setHijacked(true);
                removeShip(ship);
                ship.turnBackToHome();
            }
            ship.setChecked(true);
        }

        ships.removeAll(shipsToRemove);
        ships.addAll(shipsToAdd);

        shipsToAdd.clear();
        shipsToRemove.clear();


        if (timestamp != 0 && (timestamp % (30 * 25)) == 0) {
            pirate = generatePirate();
        }
    }

    /**
     * Metoda vykreslující objekt
     *
     * @param g Reference na grafický kontext
     */
    @Override
    public void render(GraphicsContext g) {
        if (isPirateDetected()) {
            g.setStroke(Color.RED);
        }
        else if(pirate){
            g.setStroke(Color.PALEVIOLETRED);
        }
        else {
            g.setStroke(Color.DARKSLATEGREY);
        }

        g.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
        renderShips(g);
    }

    /**
     * Pomocná metoda pro vyrenderování lodí
     * Vypočítá nové pozice a vykresíl lodě
     *
     * @param g Reference na grafický kontext
     */
    private void renderShips(GraphicsContext g) {
        double x1, y1, x2, y2, sx, sy, dv;
        BasePlanet sourcePlanet, destinationPlanet;

        for (IShip ship : ships) {
            destinationPlanet = ship.getNextDestination();
            if (destinationPlanet == a)
                sourcePlanet = b;
            else
                sourcePlanet = a;

            x1 = sourcePlanet.getX();
            y1 = sourcePlanet.getY();
            x2 = destinationPlanet.getX();
            y2 = destinationPlanet.getY();

            sx = x2 - x1;
            sy = y2 - y1;

            dv = Math.sqrt(sx * sx + sy * sy);

            sx /= dv;
            sy /= dv;

            int shipProgress = ship.getConnectionProgress();
            double progress = (weight - shipProgress);

            ship.setX(x1 + sx * progress);
            ship.setY(y1 + sy * progress);
            ship.render(g);
        }
    }

    /**
     * Nastaví stav zabezpečení cesty
     *
     * @param pirateDetected stav bezpečnosti cesty
     */
    public void setPirateDetected(boolean pirateDetected) {
        this.pirateDetected = pirateDetected;
    }

    /**
     * Získá zabezpečení cesty
     *
     * @return zapezpečení cesty
     */
    public boolean isPirateDetected() {
        return pirateDetected;
    }

    @Override
    public String toString() {
        return String.format("Path{Source planet: %s, Destination planet: %s, dangerous: %d", a, b, dangerous);
    }
}
