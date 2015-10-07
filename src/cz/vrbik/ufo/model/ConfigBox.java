package cz.vrbik.ufo.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Schránka pro nastavení
 */
public final class ConfigBox {

    public final ObjectProperty<Number> galaxyWidth;
    public final ObjectProperty<Number> galaxyHeight;

    public ConfigBox(int galaxyWidth, int galaxyHeight) {
        this.galaxyWidth = new SimpleObjectProperty<>(galaxyWidth);
        this.galaxyHeight = new SimpleObjectProperty<>(galaxyHeight);

    }
}
