package controller.asteroid;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;
import model.asteroid.FloatingItems;
import model.asteroid.Meteor;
import model.asteroid.Player;
import model.asteroid.Projectile;

import java.util.Vector;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class GamePanel extends AnchorPane {

    Vector<Meteor> enemy = new Vector<>();
    Vector<Projectile> projectiles = new Vector<>();
    Player player = new Player(500, 500, 0, 0, 0);

    boolean z_flag = false;
    boolean q_flag = false;
    boolean d_flag = false;
    boolean space_flag = false;

    Semaphore updating = new Semaphore(1, true);

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
                    case " ":
                        if (!space_flag){
                            space_flag = true;
                        }
                        break;
                }
            }
        };

        EventHandler<KeyEvent> keyReleasedHandler = new EventHandler<>() {
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
                    case " ":
                        if (space_flag){
                            space_flag = false;
                        }
                        break;
                }

            }
        };

        getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyPressedHandler);
        getScene().addEventHandler(KeyEvent.KEY_RELEASED, keyReleasedHandler);
    }


    public void start(){

        Meteor m = new Meteor(20, 20, 1, 1, 3);
        enemy.add(m);
        getChildren().add(m.get_sprite());
        getChildren().add(player.get_sprite());
        timerThread.start();
    }

    void update(){

        if (updating.tryAcquire()){
            player.handle_inputs(z_flag, d_flag, q_flag);
            player.update();
            if (player.can_shoot() & space_flag){
                Projectile p = player.shoot();
                projectiles.add(p);
                getChildren().add(p.get_sprite());
            }
            for (Projectile p : projectiles){
                p.update();
                if (p.is_dead()){
                    getChildren().remove(p.get_sprite());
                    projectiles.remove(p);
                }
                else{
                    for (Meteor meteor : enemy){
                        if (p.is_collision(meteor)){
                            remove_meteor(meteor);
                            getChildren().remove(p.get_sprite());
                            projectiles.remove(p);
                        }
                    }
                }

            }
            for (Meteor meteor : enemy) {
                meteor.update();
                if (player.is_alive() & meteor.is_collision(player)){
                    if (!player.lose_life()){
                        //gameover
                    }
                    else{

                    }

                    //for collision with bullet only



                }
            }
        }
        updating.release();


    }

    private void remove_meteor(Meteor meteor){
        getChildren().remove(meteor.get_sprite());
        enemy.remove(meteor);
        int new_size = meteor.get_size()-1;
        if (new_size > 0){
            Meteor baby = meteor.break_it();
            enemy.add(baby);
            getChildren().add(baby.get_sprite());
            baby = meteor.break_it();
            enemy.add(baby);
            getChildren().add(baby.get_sprite());
        }
    }




}
