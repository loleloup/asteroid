package view.asteroid;

import controller.asteroid.GamePanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApplication extends Application {

    Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        VBox box = new VBox();
        GamePanel pane = new GamePanel(stage, 1080, 720);
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