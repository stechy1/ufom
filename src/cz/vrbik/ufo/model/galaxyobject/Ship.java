package cz.vrbik.ufo.model.galaxyobject;

import cz.vrbik.ufo.model.BaseObject;
import cz.vrbik.ufo.model.Galaxy;
import javafx.scene.canvas.GraphicsContext;

/**
 * Třída představující dopravní loď
 */
public class Ship extends BaseObject {


    public Ship(double x, double y, Galaxy galaxy) {
        super(x, y, galaxy);
        setVisible(false);
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

    }
}
