package cz.vrbik.ufo.controller;

import cz.vrbik.ufo.model.Config;
import cz.vrbik.ufo.model.ConfigBox;
import cz.vrbik.ufo.model.Galaxy;
import cz.vrbik.ufo.model.PlanetNames;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Hlavní kontroler
 */
public class MainController implements Initializable {

    @FXML
    private VBox container;

    @FXML
    private VBox propertyVBox;
    @FXML
    private Spinner<Number> spinnerGalaxyWidth;
    @FXML
    private Spinner<Number> spinnerGalaxyHeight;
    @FXML
    private Spinner<Number> spinnerPlanetCount;
    @FXML
    private Spinner<Number> spinnerStationCount;
    @FXML
    private Spinner<Number> spinnerPlanetSpacing;
    @FXML
    private Spinner<Number> spinnerStationSpacing;

    @FXML
    private ComboBox comboboxPlanets;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Canvas canvas;

    private final Duration duration = Duration.millis(1000);
    private final KeyFrame oneFrame = new KeyFrame(duration, new MyHandler());
    private Timeline timeline;
    private Config config;
    private Galaxy galaxy;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root galaxyobject, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root galaxyobject, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        config = new Config();

        Bindings.bindBidirectional(spinnerGalaxyWidth.getValueFactory().valueProperty(), config.galaxyWidth);
        Bindings.bindBidirectional(spinnerGalaxyHeight.getValueFactory().valueProperty(), config.galaxyHeight);
        Bindings.bindBidirectional(spinnerPlanetCount.getValueFactory().valueProperty(), config.planetCount);
        Bindings.bindBidirectional(spinnerStationCount.getValueFactory().valueProperty(), config.stationCount);
        Bindings.bindBidirectional(spinnerPlanetSpacing.getValueFactory().valueProperty(), config.planetSpacing);
        Bindings.bindBidirectional(spinnerStationSpacing.getValueFactory().valueProperty(), config.stationSpacing);

        SplitPane.setResizableWithParent(propertyVBox, false);

        scrollPane.setPrefSize(300, 300);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-focus-color: transparent;");

        galaxy = new Galaxy(config);


        Platform.runLater(() -> {
            canvas.requestFocus();
            timeline = new Timeline(oneFrame);
            timeline.setCycleCount(Animation.INDEFINITE);
        });
    }

    private void showCreateGalaxyDialog() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Informace");
        a.setContentText("Před akcí musíte nejdříve galaxii vygenerovat");
        a.showAndWait();
    }

    private void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        galaxy.render(g);
    }

    /**
     * Reakce na tlačítko generovat data
     */
    public void generateDataButtonHandler(){
        PlanetNames.getInstance().shuffleNames();
        galaxy.bigBang();
        config.saveConfig();

        galaxy = new Galaxy(config);
        galaxy.generate();

        draw();
    }

    /**
     * Ošetření pro kliknutí na tlačítko start
     */
    public void startSimulationButtonHandler() {
        if (galaxy == null) {
            showCreateGalaxyDialog();
            return;
        }

        timeline.play();
    }

    /**
     * Reakce na tlačítko exportovat galaxii
     */
    public void exportGalaxyButtonHandler() {
        if (galaxy == null) {
            showCreateGalaxyDialog();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Galaxy files", "*.glx"));
        File file = fileChooser.showSaveDialog(container.getScene().getWindow());
        if (file != null) {
            if (galaxy.exportGalaxy(file)) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Informace");
                a.setContentText("Galaxie byla úspěšně vyexportována");
                a.showAndWait();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Chyba");
                a.setContentText("Galaxie nebyla úspěšně vyexportována");
                a.showAndWait();
            }
        }
    }

    public void importGalaxyButtonHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Galaxy files", "*.glx"));
        File file = fileChooser.showOpenDialog(container.getScene().getWindow());
        if (file != null) {
            if (galaxy.importGalaxy(file)) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Informace");
                a.setContentText("Galaxie byla úspěšně načtena");
                a.showAndWait();
                draw();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Chyba");
                a.setContentText("Galaxie nebyla úspěšně načtena");
                a.showAndWait();
            }
        }
    }


    class MyHandler implements EventHandler<ActionEvent> {

        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(ActionEvent event) {
            System.out.println("Handle event");
            draw();
        }
    }
}
