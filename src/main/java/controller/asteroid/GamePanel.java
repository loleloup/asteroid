package controller.asteroid;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;
import model.asteroid.Meteor;
import model.asteroid.Player;

import java.util.Vector;

import static java.lang.Thread.sleep;

public class GamePanel extends AnchorPane {

    Vector<Meteor> ennemy = new Vector<>();
    Player player = new Player(50, 50, 0, 0, 0);

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

    public GamePanel(){}


    public void start(){

        Meteor m = new Meteor(20, 20, 1, 1);
        ennemy.add(m);
        getChildren().add(m.get_sprite());
        getChildren().add(player.get_sprite());
        timerThread.start();
    }

    void update(){
        for (Meteor meteor : ennemy) {
            meteor.update();
        }
    }






}
