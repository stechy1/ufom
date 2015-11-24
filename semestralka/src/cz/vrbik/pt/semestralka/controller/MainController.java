package cz.vrbik.pt.semestralka.controller;

import cz.vrbik.pt.semestralka.model.control.PlanetInspector;
import cz.vrbik.pt.semestralka.model.galaxy.Galaxy;
import cz.vrbik.pt.semestralka.model.galaxy.planet.Planet;
import cz.vrbik.pt.semestralka.model.service.Config;
import cz.vrbik.pt.semestralka.model.service.TextAreaAdapter;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Třída reagující na stav uživatele
 */
public class MainController implements Initializable {

    private static final Logger log = Logger.getLogger(MainController.class.getName());
    private static final int CANVAS_PADDING = 100;
    private static final int MAP_MARGIN = 50;



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
    private Spinner<Number> spinnerShipsPerStation;

    @FXML
    private VBox vBoxInspector;
    @FXML
    private ComboBox<Planet> comboboxPlanets;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Canvas canvas;
    @FXML
    private TextArea textAreaLog;

    @FXML
    private Label labelDay;
    @FXML
    private Label labelMonth;
    @FXML
    private Label labelYear;
    @FXML
    private HBox hBoxProgressBarContainer;
    @FXML
    private ProgressBar progressBarGalaxyProgress;
    @FXML
    private Label labelRemaining;

    private final Duration duration = Duration.millis(30);
    private final KeyFrame oneFrame = new KeyFrame(duration, event -> update());
    private Timeline timeline;
    private Config config;
    private Galaxy galaxy;
    private File usedFile;
    private final PlanetInspector planetInspector = new PlanetInspector();
    private int counter, day, month, year, rok;
    private double translateX, translateY;
    private Planet selectedPlanet;

    private void draw(GraphicsContext g) {
        g.setFill(Color.BLACK);
        g.fillRect(-translateX, -translateY, canvas.getWidth(), canvas.getHeight());
        //g.clearRect(-translateX, -translateY, canvas.getWidth(), canvas.getHeight());

        galaxy.render(g);
    }

    private void scale(GraphicsContext g) {
        g.restore();
        g.save();
        double scaleX = (canvas.getWidth() - CANVAS_PADDING) / config.getGalaxyWidth();
        double scaleY = (canvas.getHeight() - CANVAS_PADDING) / config.getGalaxyHeight();
        g.scale(scaleX, scaleY);
        translateX = (int) (config.getGalaxyWidth() / (canvas.getWidth() - CANVAS_PADDING / scaleX) + MAP_MARGIN / scaleX);
        translateY = (int) (config.getGalaxyHeight() / (canvas.getHeight() - CANVAS_PADDING / scaleY) + MAP_MARGIN / scaleY);
        //System.out.println("scaleX: " + scaleX + ", scaleY: " + scaleY + ", translateX: " + translateX + ", translateY: " + translateY);
        g.translate(translateX, translateY);
    }

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
        TextAreaAdapter.ta = textAreaLog;
        log.info("Inicializuji grafické rozhraní...");
        config = new Config();
        canvas.getGraphicsContext2D().save();

        Bindings.bindBidirectional(spinnerGalaxyWidth.getValueFactory().valueProperty(), config.galaxyWidth);
        Bindings.bindBidirectional(spinnerGalaxyHeight.getValueFactory().valueProperty(), config.galaxyHeight);
        Bindings.bindBidirectional(spinnerPlanetCount.getValueFactory().valueProperty(), config.planetCount);
        Bindings.bindBidirectional(spinnerStationCount.getValueFactory().valueProperty(), config.stationCount);
        Bindings.bindBidirectional(spinnerPlanetSpacing.getValueFactory().valueProperty(), config.planetSpacing);
        Bindings.bindBidirectional(spinnerStationSpacing.getValueFactory().valueProperty(), config.stationSpacing);
        Bindings.bindBidirectional(spinnerShipsPerStation.getValueFactory().valueProperty(), config.shipCount);


        hBoxProgressBarContainer.widthProperty().addListener((observable, oldValue, newValue) -> progressBarGalaxyProgress.setPrefWidth(newValue.doubleValue()));

        SplitPane.setResizableWithParent(propertyVBox, false);

        scrollPane.setPrefSize(300, 300);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-focus-color: transparent;");

        galaxy = new Galaxy(config);
        comboboxPlanets.setItems(galaxy.getPlanets());
        comboboxPlanets.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedPlanet = newValue;
            planetInspector.setPlanet(newValue);
        });

        Platform.runLater(() -> {
            vBoxInspector.getChildren().add(1, planetInspector.getContainer());
            canvas.requestFocus();
            timeline = new Timeline(oneFrame);
            timeline.setCycleCount(Animation.INDEFINITE);
        });
    }

    /**
     * Reakce na tlačítko generovat galaxii
     */
    public void generateGalaxyButtonHandler() {
        progressBarGalaxyProgress.setProgress(0);
        counter = day = month = year = 0;
        rok = 360;
        galaxy.generate();
        GraphicsContext g = canvas.getGraphicsContext2D();
        scale(g);
        draw(g);
    }

    /**
     * Reakce na tlačítko importovat galaxii
     */
    public void importGalaxyButtonHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Galaxy files", "*.glx"));
        File file = fileChooser.showOpenDialog(container.getScene().getWindow());
        if (file != null) {
            if (galaxy.restore(file)) {
                log.info("Import galaxie byl úspěšný");
                usedFile = file;
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Informace");
                a.setContentText("Galaxie byla úspěšně načtena");
                a.showAndWait();
                scale(canvas.getGraphicsContext2D());
                draw(canvas.getGraphicsContext2D());
            } else {
                log.error("Import galaxie nebyl úspěšný");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Chyba");
                a.setContentText("Galaxie nebyla úspěšně načtena");
                a.showAndWait();
            }
        }
    }

    /**
     * Reakce na tlačítko uložit
     */
    public void exportGalaxyButtonHandler() {

        if (usedFile == null) {
            exportAsGalaxyButtonHandler();
            return;
        }

        if (galaxy.store(usedFile)) {
            log.info("Export galaxie byl úspěšný");
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Informace");
            a.setContentText("Galaxie byla úspěšně vyexportována");
            a.showAndWait();
        } else {
            log.error("Export galaxie nebyl úspěšný");
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Chyba");
            a.setContentText("Galaxie nebyla úspěšně vyexportována");
            a.showAndWait();
        }
    }

    /**
     * Reakce na tlačítko uložit jako...
     */
    public void exportAsGalaxyButtonHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Galaxy files", "*.glx"));
        File file = fileChooser.showSaveDialog(container.getScene().getWindow());
        if (file != null) {
            usedFile = file;
            exportGalaxyButtonHandler();
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Nebyl vybrán žádný soubor");
            a.showAndWait();
        }
    }

    /**
     * Reakce na tlačítko exit
     */
    public void exitButtonHandler() {
        if (usedFile != null) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setHeaderText("Chcete uložit galaxii před ukončením?");

            ButtonType yesBtn = new ButtonType("Ano");
            ButtonType noBtn = new ButtonType("Ne");
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            a.getButtonTypes().setAll(yesBtn, noBtn, cancelBtn);

            Optional<ButtonType> result = a.showAndWait();
            if (result.get() == yesBtn) {
                exportGalaxyButtonHandler();
            } else if (result.get() == cancelBtn) {
                return;
            }
        }
        timeline.stop();
        log.info("Ukončuji činnost");
        Platform.exit();
    }

    /**
     * Reakce na tlačítko spustit simulaci
     */
    public void runSimulationButtonHanler() {
        timeline.play();
    }

    /**
     * Reakce na tlačítko zastavit simulaci
     */
    public void stopSimulationButtonHandler() {
        timeline.pause();
    }

    /**
     * Reakce na tlačítko manuální update
     */
    public void makeUpdateButtonHandler() {
        update();
    }

    public void newRequestButtonHandle() {
        if (selectedPlanet != null)
            selectedPlanet.makeManualRequest();
    }

    protected void update(){
        galaxy.update(counter++);
        draw(canvas.getGraphicsContext2D());

        progressBarGalaxyProgress.setProgress(counter / 9000.0);
        if (counter % 25 == 0) {
            day++;
            labelDay.setText("Den: " + day);
            labelRemaining.setText("Zbývá: " + (--rok) + " dni.");
            if(rok == 0){rok = 360;}
        }

        if (day % 30 == 0 && day != 0) {
            month++;
            labelMonth.setText("Měsíc: " + month);
        }

        if (month % 12 == 0 && month != 0) {
            year++;
            labelYear.setText("Rok: " + year);
        }

        if (day == 30)
            day = 0;

        if (month == 12)
            month = 0;
    }

    public void defaultButtonHandler() {
        config.setDefault();
    }
}
