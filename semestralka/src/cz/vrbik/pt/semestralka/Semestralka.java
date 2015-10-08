package cz.vrbik.pt.semestralka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import java.net.URL;
import java.util.Objects;

/**
 * Vstupní třída semestrální práce na PT
 */
public class Semestralka extends Application {

    private static final Logger log = Logger.getLogger(Semestralka.class.getName());

    public static final URL PATH_TO_LOG4J_PROPERTIES = ClassLoader.getSystemResource("resources/config/log4j.properties");

    private static boolean nogui = false;
    private static String openFilePath = "";

    public static void main(String[] args) {
        PropertyConfigurator.configure(PATH_TO_LOG4J_PROPERTIES);

        if (args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                if (Objects.equals(args[i], "-nogui"))
                    nogui = true;
                if (Objects.equals(args[i], "-file")) {
                    if ((i + 1) != args.length)
                        openFilePath = args[i + 1];
                }
            }
        }

        if (nogui) {
            log.info("Spouštím galaxii v konzoli...");
        } else {
            log.info("Spouštím grafického klienta galaxie...");
            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent p =  FXMLLoader.load(ClassLoader.getSystemResource("resources/fxml/main.fxml"));

        Scene scene = new Scene(p);

        primaryStage.setScene(scene);
        primaryStage.setTitle("UFO - Medical");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }
}
