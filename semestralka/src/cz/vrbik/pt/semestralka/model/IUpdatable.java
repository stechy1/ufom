package cz.vrbik.pt.semestralka.model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Rozhraní definující metody pro aktualizaci stavu logiky objektu a jeho vykreslení
 */
public interface IUpdatable {

    /**
     * Metoda aktualizující logiku objektu
     * @param timestamp Doba od spuštění simulace
     */
    void update(int timestamp);

    /**
     * Metoda vykreslující objekt
     * @param g Reference na grafický kontext
     */
    void render(GraphicsContext g);
}
