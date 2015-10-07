package cz.vrbik.ufo.model.galaxyobject;

import cz.vrbik.ufo.model.Galaxy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * Tída reprezentující jednu základnu ve vesmíru
 */
public class Station extends BasePlanet {

    public static final int DEFAULT_WIDTH = 8;
    public static final int DEFAULT_HEIGHT = 8;

    private static int ID_COUNTER = -1;

    public static void resetCounter() {
        ID_COUNTER = -1;
    }

    /**
     * Vytvoří novou instanci z textové podoby informaci
     * @param line Řádka s informacemi
     * @param galaxy Reference na danou galaxii
     * @return Novou instanci stanice
     */
    public static Station getStationFromString(String line, Galaxy galaxy) {

        line = line.replaceAll(" ", "");
        String[] data = line.split(";");
        Double x  = Double.parseDouble((data[0].split(":"))[1]);
        Double y  = Double.parseDouble((data[1].split(":"))[1]);
        int id = Integer.parseInt((data[2].split(":"))[1]);
        String name = (data[3].split(":"))[1];

        String[] neighbours = ((data[4].split(":"))[1]).split(",");
        Integer[] intNeighbours = new Integer[neighbours.length];

        for (int i = 0; i < neighbours.length; i++) {
            intNeighbours[i] = Integer.parseInt(neighbours[i]);
        }

        return new Station(galaxy, x, y, id, name, intNeighbours);
    }

    private Station(Galaxy galaxy, double x, double y, int id, String name, Integer[] neighbours) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, galaxy);
        this.ID = id;
        this.name = name;
        this.rawNeighbours.addAll(Arrays.asList(neighbours));
    }

    /**
     * Creates a new instance of Rectangle with the given position and size.
     *
     * @param x      horizontal position of the rectangle
     * @param y      vertical position of the rectangle
     * @param galaxy Reference na galaxii
     */
    public Station(double x, double y, Galaxy galaxy) {
        this(x, y, galaxy, "MS" + ID_COUNTER);
    }

    /**
     * Creates a new instance of Rectangle with the given position and size.
     *
     * @param x      horizontal position of the rectangle
     * @param y      vertical position of the rectangle
     * @param galaxy Reference na galaxii
     * @param name   Název záchrané stanice
     */
    public Station(double x, double y, Galaxy galaxy, String name) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, galaxy);
        ID = ID_COUNTER--;
        this.name = name;
    }

    /**
     * Metoda pro aktualizaci logiky simulace
     */
    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext g) {
        g.setStroke(Color.RED);
        for (BasePlanet neighbour : neighbours) {
            g.strokeLine(getX() + TRANSLATE_X, getY() + TRANSLATE_Y, neighbour.getX() + TRANSLATE_X, neighbour.getY() + TRANSLATE_Y);
        }

        g.setFill(Color.LIME);
        super.render(g);
    }
}
