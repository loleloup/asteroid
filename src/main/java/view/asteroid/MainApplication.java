package view.asteroid;

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

    Polygon polygon;
    Stage stage;
    Double x = 50.0;
    Thread timerThread = new Thread(() -> {
        while (true) {
            try {
                sleep(17); //60Hz
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(this::update);
        }
    });

    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane, 1080, 720);

        Meteor m = new Meteor(0, 0, 0, 0);
        System.out.println(Arrays.toString(m.get_points()));
        polygon = new Polygon();
        polygon.getPoints().addAll(m.get_points());
        polygon.setTranslateY(75.0);



        pane.getChildren().add(polygon);
        this.stage = stage;
        this.stage.setTitle("Asteroid");
        this.stage.setScene(scene);
        this.stage.show();

        timerThread.start();


    }

    private void update() {
        System.out.println("updating");
        polygon.setTranslateX(x);
        x += 2;

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        timerThread.join();
    }

    public static void main(String[] args) {
        launch();
    }
}