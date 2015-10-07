package cz.vrbik.ufo.model.galaxyobject;

import cz.vrbik.ufo.model.Galaxy;
import cz.vrbik.ufo.model.PlanetNames;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * Třída představující jednu planetu
 */
public class Planet extends BasePlanet {

    public static final int DEFAULT_WIDTH = 2;
    public static final int DEFAULT_HEIGHT = 2;

    private static int ID_COUNTER = 0;
    private static final int MAX_POPULATION = 10000000;
    private static final int MIN_POPULATION = 100000;
    private static final PlanetNames names = PlanetNames.getInstance();

    private int inhabitants = 0;

    public static void resetCounter() {
        ID_COUNTER = 0;
    }

    public static Planet getPlanetFromString(String line, Galaxy galaxy) {
        line = line.replace(" ", "");
        String[] data = line.split(";");

        String[] rawX = data[0].split(":");
        String[] rawY = data[1].split(":");
        String[] rawID = data[2].split(":");
        String[] rawName = data[3].split(":");
        String[] rawNei = data[4].split(":");
        String[] rawNeighbours = rawNei[1].split(",");
        String[] rawInhab = data[5].split(":");

        double x = Double.parseDouble(rawX[1]);
        double y = Double.parseDouble(rawY[1]);
        int id = Integer.parseInt(rawID[1]);
        String name = rawName[1];
        int inhabitants = Integer.parseInt(rawInhab[1]);

        Integer[] neighbours = new Integer[rawNeighbours.length];
        //TODO zjistit, jak lepe namapovat stringový pole na integerový
        for (int i = 0; i < neighbours.length; i++) {
            neighbours[i] = Integer.parseInt(rawNeighbours[i]);
        }

        return new Planet(galaxy, x, y, id, name, inhabitants, neighbours);
    }

    private Planet(Galaxy galaxy, double x, double y, int id, String name, int inhabitants, Integer[] neighbours) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, galaxy);
        this.ID = id;
        this.name = name;
        this.inhabitants = inhabitants;
        this.rawNeighbours.addAll(Arrays.asList(neighbours));
    }

    /**
     *
     * @param x
     * @param y
     * @param galaxy
     */
    public Planet(double x, double y, Galaxy galaxy) {
        this(x, y, galaxy, names.getName(), generateInhabitants());
    }

    /**
     *
     * @param x
     * @param y
     * @param galaxy
     * @param name
     * @param inhabitants
     */
    public Planet(double x, double y, Galaxy galaxy, String name, int inhabitants) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, galaxy);
        ID = ID_COUNTER++;
        this.name = name;
        this.inhabitants = inhabitants;
    }

    /**
     * Metoda pro generování počtu původních obyvatel
     */
    private static int generateInhabitants() {
        return MIN_POPULATION + (int)(Math.random() * ((MAX_POPULATION - MIN_POPULATION) + 1));
    }

    /**
     * Metoda pro aktualizaci logiky simulace
     */
    @Override
    public void update() {

    }

    /**
     * Metoda pro vykreslení aktuálního stavu objektu
     *
     * @param g Grafický kontext
     */
    @Override
    public void render(GraphicsContext g) {
        g.setStroke(Color.RED);

       // for (BasePlanet neighbour : neighbours) {
       //     g.strokeLine(getX() + TRANSLATE_X, getY() + TRANSLATE_Y, neighbour.getX() + TRANSLATE_X, neighbour.getY() + TRANSLATE_Y);
       // }

        g.setFill(Color.WHITE);
        super.render(g);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("; Inhabitants: ").append(inhabitants);

        return sb.toString();
    }


}
