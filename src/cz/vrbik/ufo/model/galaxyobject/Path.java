package cz.vrbik.ufo.model.galaxyobject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Vrbik on 28.09.2015.
 */
public class Path {

    public static final int TRANSLATE_X = 50;
    public static final int TRANSLATE_Y = 50;

    public double weight = 0;

    private static int ID_COUNTER = 0;
    private double cost = 0;
    private int id;

    public BasePlanet a;
    public BasePlanet b;

    public Path(BasePlanet a, BasePlanet b){
        id = ID_COUNTER++;
        this.a = a;
        this.b = b;

        a.addPath(this);
        b.addPath(this);

        this.weight = planetDistance(a, b);
    }

    public BasePlanet getNeighbour(BasePlanet x) {
        if(x == a) {
            return b;
        }
        else if(x == b) {
            return a;
        }

        return null;
    }


    /**
     * Vrátí vzdálenost dvou planet
     * @param a Planeta A První planeta
     * @param b Planeta B Druhá planeta
     * @return vzdálenost od A do B
     */
    private double planetDistance(BasePlanet a, BasePlanet b) {
        return Math.sqrt(Math.pow((b.getX() - a.getX()), 2) + Math.pow((b.getY() - a.getY()), 2));
    }

    public void render(GraphicsContext g) {
        if(weight > 30)
        g.setStroke(Color.YELLOW);
        else{
            g.setStroke(Color.RED);
        }
        g.strokeLine(a.getCenterX() + TRANSLATE_X, a.getCenterY() + TRANSLATE_Y, b.getCenterX() + TRANSLATE_X, b.getCenterY() + TRANSLATE_Y);
    }

    public int getID() {
        return id;
    }

    public double getWeight() {
        return weight;
    }
}
