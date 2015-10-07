package cz.vrbik.ufo.model.galaxyobject;

import cz.vrbik.ufo.model.BaseObject;
import cz.vrbik.ufo.model.Galaxy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Společný předek pro stanici a planety
 * Stanice je speciální případ planety
 */
public abstract class BasePlanet extends BaseObject implements Comparable<BasePlanet>{

    public static int NEIGHBOURS_COUNT = 5;

    protected final Set<BasePlanet> neighbours;
    protected final Set<Integer> rawNeighbours;
    public final Set<Path> paths;

    public BasePlanet previous;

    public double minDistance = Double.POSITIVE_INFINITY;

    protected int ID;
    protected String name;

    public final List<Ship> dockedShips = new ArrayList<>();


    /**
     * Creates a new instance of Rectangle with the given position and size.
     *
     * @param x      horizontal position of the rectangle
     * @param y      vertical position of the rectangle
     * @param width  width of the rectangle
     * @param height height of the rectangle
     * @param galaxy
     */
    public BasePlanet(double x, double y, double width, double height, Galaxy galaxy) {
        super(x, y, width, height, galaxy);

        paths = new HashSet<>(NEIGHBOURS_COUNT);
        neighbours = new HashSet<>(NEIGHBOURS_COUNT);
        rawNeighbours = new HashSet<>(NEIGHBOURS_COUNT);
    }

    /**
     * Metoda zjišťující, zda-li může planeta přidat obchodní cestu
     *
     * @return True, pokud má planeta volné místo pro přidání obchodní cesty, jinak false
     */
    public boolean isFull() {
        return paths.size() == 5;
    }

    /**
     * Vrátí počet volných pozic pro sousedy
     *
     * @return počet volných pozic
     */
    public int getFreeSlots() {
        return NEIGHBOURS_COUNT - paths.size();
    }


    /**
     * Přidá cestu
     * @param p
     */
   public void addPath(Path p){
        paths.add(p);
       // neighbours.add(p.getNeighbour(this));
    }

    public int getID(){
        return ID;
    }

    @Override
    public String toString() {

        String neiIDs = neighbours.stream()
                .map(neighbour -> Integer.toString(neighbour.ID))
                .collect(Collectors.joining(", "));

        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("; ID: ").append(ID);
        sb.append("; Name: ").append(name);
        sb.append("; Neighbours: ").append(neiIDs);

        return sb.toString();
    }

    public int compareTo(BasePlanet other){
        return Double.compare(minDistance, other.minDistance);
    }


}
