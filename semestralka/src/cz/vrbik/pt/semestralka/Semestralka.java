package cz.vrbik.pt.semestralka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Camera;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.URL;

/**
 * Vstupní třída semestrální práce na PT
 */
public class Semestralka extends Application {

    private static final Logger log = Logger.getLogger(Semestralka.class.getName());

    public static final URL PATH_TO_LOG4J_PROPERTIES = ClassLoader.getSystemResource("resources/config/log4j.properties");


    public static void main(String[] args) {
        PropertyConfigurator.configure(PATH_TO_LOG4J_PROPERTIES);

        log.info("Spouštím grafického klienta galaxie...");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent p =  FXMLLoader.load(ClassLoader.getSystemResource("resources/fxml/main.fxml"));
        Scene scene = new Scene(p);

        primaryStage.setScene(scene);
        primaryStage.setTitle("UFO - Medical");
        primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("resources/icon/ufo-medical.png")));
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }
}
