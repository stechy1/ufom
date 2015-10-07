package cz.vrbik.pt.semestralka.model.galaxy.planet;

import cz.vrbik.pt.semestralka.model.IUpdatable;
import cz.vrbik.pt.semestralka.model.galaxy.Galaxy;
import cz.vrbik.pt.semestralka.model.galaxy.IGraphicalObject;
import cz.vrbik.pt.semestralka.model.galaxy.Path;
import cz.vrbik.pt.semestralka.model.galaxy.ship.BaseShip;
import cz.vrbik.pt.semestralka.model.galaxy.ship.IShip;
import javafx.scene.canvas.GraphicsContext;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Základní třída pro planetu
 */
public abstract class BasePlanet
        implements IUpdatable, IShipControll, IPathControll, ISlotControll, IGraphicalObject, Comparable<BasePlanet> {

    private static final Logger log = Logger.getLogger(BasePlanet.class.getName());

    public static final int MAX_NEIGHBOURS = 5;
    public static final int DEFAULT_WIDTH = 1;
    public static final int DEFAULT_HEIGHT = 1;

    public final List<Path> paths = new ArrayList<>();
    public final List<IShip> dockedShips = new ArrayList<>();
    protected final List<IShip> shipsReadyToGo = new ArrayList<>();
    protected final List<IShip> emptyShips = new ArrayList<>();

    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected int id;
    protected String name;

    public double minDistance = Double.POSITIVE_INFINITY;
    public BasePlanet previous;

    /**
     * Konstruktor třídy {@link BasePlanet} s výchozí výškou a šířkou objektu
     *
     * @param x Vodorovná souřadnice objektu
     * @param y Svislá souřadnice objektu
     */
    public BasePlanet(double x, double y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Konstruktor třídy {@link BasePlanet}
     *
     * @param x Vodorovná souřadnice objektu
     * @param y Svislá souřadnice objektu
     * @param width Šířka objektu
     * @param height Výška objektu
     */
    public BasePlanet(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Zjistí vzdálenost mezi dvěma zadanými planetami
     *
     * @param a Reference na první planetu
     * @param b Reference na druhou planetu
     * @return Vzdálenost mezi dvěma planetami
     */
    public static double planetDistance(BasePlanet a, BasePlanet b) {
        return Math.sqrt(Math.pow((b.x - a.x), 2) + Math.pow((b.y - a.y), 2));
    }

    /**
     * Vzdálenost od zadané planety
     *
     * @param planet Reference na planetu
     * @return Vzdálenost mezi zadanou a instancovanou planetou
     */
    public double planetDistance(BasePlanet planet) {
        return planetDistance(this, planet);
    }

    /**
     * Metoda zjišťuje, zda-li objekt má volný slot pro spojení
     *
     * @return True, pokud je slot volný, jinak false
     */
    @Override
    public boolean isFull() {
        return paths.size() == MAX_NEIGHBOURS;
    }

    /**
     * Metoda vrací počet volných slotů
     *
     * @return Počet volných slotů
     */
    @Override
    public int getFreeSlots() {
        return MAX_NEIGHBOURS - paths.size();
    }

    /**
     * Přidá cestu do seznamu planety
     *
     * @param path Cesta vedoucí z planety
     */
    @Override
    public void addPath(Path path) {
        paths.add(path);
    }

    /**
     * Metoda aktualizující logiku objektu
     *
     * @param timestamp Doba od spuštění simulace
     */
    @Override
    public void update(int timestamp) {
        shipsReadyToGo.forEach(IShip::continueTrip);
        shipsReadyToGo.clear();

        for (IShip emptyShip : emptyShips) {
            emptyShip.schedule();
            emptyShip.startTrip();
        }

        for (IShip dockedShip : dockedShips) {
            dockedShip.unLoadCargo(dockedShip.getCargo());
        }

        emptyShips.clear();
        emptyShips.addAll(dockedShips.stream().collect(Collectors.toList()));

        dockedShips.removeAll(emptyShips);
    }

    /**
     * Metoda vykreslující objekt
     *
     * @param g Reference na grafický kontext
     */
    @Override
    public void render(GraphicsContext g) {
        g.fillRect(x + Galaxy.TRANSLATE_X, y + Galaxy.TRANSLATE_Y, width, height);
    }

    /**
     * Nadokuje loď
     *
     * @param ship
     */
    @Override
    public void dock(IShip ship) {
        if (ship.isEndOfTrip()) {
            dockedShips.add(ship);
            log.info(String.format("Parkuji loď: %s na planetě: %s", ship, name));
            //System.out.println("Parkuji loď: " + ship + " na planetě: " + this);
        }
        else {
            log.debug("Připravuji loď " + ship + " na další cestu");
            //System.out.println("Připravuji loď na další cestu");
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
        BasePlanet neighbour = null;
        Path myPath = null;
        for (Path path : paths)
            if ((neighbour = path.getNeighbour(endPoint)) != null) {
                myPath = path;
                break;
            }

//        Teoreticky by nikdy nemělo nastat
        if (neighbour == null) {
            log.error("Nemám loď " + ship + " kam poslat");
            //System.out.println("Nemám kam poslat loď");
            return;
        }

        ship.setConnectionProgress((int) myPath.weight);
        ship.setChecked(false);
        myPath.addShip(ship);
        log.debug(String.format("Posílám loď %s na cestu: %s. Cíl cesty: %s", ship, myPath, endPoint.getName()));
        //System.out.println("Posílám loď na cestu: " + paths + "\nCíl cesty: " + endPoint);
    }

    /**
     * Metoda sestaví sousedy planety
     *
     * @return Řetězec planet definovaných jejich IDčkama
     */
    public String exportNeighbours() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(":");
        for (Path path : paths) {
            BasePlanet b = path.getNeighbour(this);
           sb.append(b.id).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Vrátí vodorovnou pozici objektu
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * Nastaví vodorovnou pozici objektu
     *
     * @param x vodorovná pozice objektu
     */
    @Override
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Vrátí svislou pozici objektu
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * Nastaví svislou pozici objektu
     *
     * @param y svislá pozice objektu
     */
    @Override
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Vrátí šířku objektu
     */
    @Override
    public double getWidth() {
        return width;
    }

    /**
     * Nastaví šířku objektu
     *
     * @param width šířku objektu
     */
    @Override
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Vrátí výšku objektu
     */
    @Override
    public double getHeight() {
        return height;
    }

    /**
     * Nastaví výšku objektu
     *
     * @param height Výška
     */
    @Override
    public void setHeight(double height) {
        this.height = height;
    }


    public String getName() {
        return name;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param other the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(BasePlanet other) {
        return Double.compare(minDistance, other.minDistance);
    }

    @Override
    public String toString() {
        return String.format("ID: %d; Name: %s", id, name);
    }
}
