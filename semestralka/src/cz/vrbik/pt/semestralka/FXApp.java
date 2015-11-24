package cz.vrbik.pt.semestralka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Vstupní bod aplikace s grafickým rozhraním
 */
public class FXApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent p = FXMLLoader.load(ClassLoader.getSystemResource("resources/fxml/main.fxml"));
        Scene scene = new Scene(p);

        primaryStage.setScene(scene);
        primaryStage.setTitle("UFO - Medical");
        primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("resources/icon/ufo-medical.png")));
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }
}
