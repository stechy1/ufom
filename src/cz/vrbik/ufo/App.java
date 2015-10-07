package cz.vrbik.ufo;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Vstupní třída aplikace
 */
public class App extends Application {
    //TODO Kompletně přepsat aplikaci na něco lépe udržovatelného

    public static void main(String[] args) {
        launch(args);
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
