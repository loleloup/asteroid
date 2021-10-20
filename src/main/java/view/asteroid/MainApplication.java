package view.asteroid;

import controller.asteroid.GamePanel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.asteroid.Meteor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static java.lang.Thread.sleep;

public class MainApplication extends Application {

    Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        GamePanel pane = new GamePanel(1080, 720);
        Scene scene = new Scene(pane, 1080, 720);
        pane.add_handlers();
        this.stage = stage;
        this.stage.setTitle("Asteroid");
        this.stage.setScene(scene);
        this.stage.show();
        pane.start();


    }


    public static void main(String[] args) {
        launch();
    }
}