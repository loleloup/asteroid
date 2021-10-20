package controller.asteroid;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;
import model.asteroid.FloatingItems;
import model.asteroid.Meteor;
import model.asteroid.Player;

import java.util.Objects;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class GamePanel extends AnchorPane {

    Vector<Meteor> ennemy = new Vector<>();
    Player player = new Player(50, 50, 0, 0, 0);

    boolean z_flag = false;
    boolean q_flag = false;
    boolean d_flag = false;

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

    public GamePanel(int width, int height){
        FloatingItems.set_wind_size(width, height);
    }

    public void add_handlers(){     //need to add the events to the scene, otherwise doesn't receive them
        EventHandler<KeyEvent> keyPressedHandler = new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String letter = keyEvent.getText();
                switch (letter) {
                    case "z":
                        if (!z_flag){
                            z_flag = true;
                        }
                        break;

                    case "d":
                        if (!d_flag){
                            d_flag = true;
                        }
                        break;
                    case "q":
                        if (!q_flag){
                            q_flag = true;
                        }
                        break;
                }
            }
        };

        EventHandler<KeyEvent> keyRealeasedHandler = new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String letter = keyEvent.getText();
                switch (letter) {
                    case "z":
                        if (z_flag){
                            z_flag = false;
                        }
                        break;

                    case "d":
                        if (d_flag){
                            d_flag = false;
                        }
                        break;
                    case "q":
                        if (q_flag){
                            q_flag = false;
                        }
                        break;
                }

            }
        };

        getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler);
        getScene().addEventHandler(KeyEvent.KEY_RELEASED, keyRealeasedHandler);
    }


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
        player.handle_inputs(z_flag, d_flag, q_flag);
        //System.out.println(player.get_angle());
        player.update();

    }






}
