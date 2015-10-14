package cz.vrbik.pt.semestralka.model.control;


import cz.vrbik.pt.semestralka.model.galaxy.planet.Planet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Třída reprezentující kontrolku zobrazující vlastnosti o dané planetě
 */
public class PlanetInspector {

    @FXML
    private VBox container;
    @FXML
    private Label labelPlanetName;
    @FXML
    private Label labelInhabitants;
    @FXML
    private Label labelGraves;
    @FXML
    private Label labelInhabitantsEndagered;

    private Planet planet;

    public PlanetInspector() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ClassLoader.getSystemResource("resources/fxml/planetInspector.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInfo() {
        labelPlanetName.setText("Název planety: " + planet.getName());
        labelInhabitants.setText("Počet obyvatel: " + planet.inhabitants);
        labelGraves.setText("Mrtvých lidí: " + planet.graves);
        labelInhabitantsEndagered.setText("Ohrožených lidí: " + planet.inhabitantsEndagered);
    }

    public VBox getContainer() {
        return container;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
        showInfo();
    }
}
