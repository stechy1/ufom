package cz.vrbik.ufo.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

/**
 * Výchozí třída pro veškeré vykreslující se objekty
 */
public abstract class BaseObject extends Rectangle {

    public static final int DEFAULT_WIDTH = 1;
    public static final int DEFAULT_HEIGHT = 1;

    public static final int TRANSLATE_X = 50;
    public static final int TRANSLATE_Y = 50;

    protected Galaxy galaxy;

    /**
     * Creates a new instance of Rectangle with the given position and size.
     *
     * @param x      horizontal position of the rectangle
     * @param y      vertical position of the rectangle
     * @param width  width of the rectangle
     * @param height height of the rectangle
     * @param galaxy reference na galaxii
     */
    public BaseObject(double x, double y, Galaxy galaxy) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, galaxy);
    }

    /**
     * Creates a new instance of Rectangle with the given position and size.
     *
     * @param x      horizontal position of the rectangle
     * @param y      vertical position of the rectangle
     * @param width  width of the rectangle
     * @param height height of the rectangle
     */
    public BaseObject(double x, double y, double width, double height, Galaxy galaxy) {
        super(x, y, width, height);
        this.galaxy = galaxy;
    }

    /**
     * Metoda pro aktualizaci logiky simulace
     */
    public abstract void update();

    /**
     * Metoda pro vykreslení aktuálního stavu objektu
     * @param g Grafický kontext
     */
    public void render(GraphicsContext g) {
        g.fillRect((getX() - getWidth() / 2) + TRANSLATE_X, (getY() - getHeight() / 2) + TRANSLATE_Y, getWidth(), getHeight());
    }

    public double getCenterX() {
        return getX() + getWidth() / 2;
    }

    public double getCenterY() {
        return getY() + getHeight() / 2;
    }

    @Override
    public String toString() {
        return "X: " + getX() + "; Y: " + getY();
    }
}
