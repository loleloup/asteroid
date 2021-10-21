package controller.asteroid;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;
import model.asteroid.FloatingItems;
import model.asteroid.Meteor;
import model.asteroid.Player;
import model.asteroid.Projectile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;



public class paneltest extends AnchorPane {

    Vector<Meteor> enemy = new Vector<>();
    Vector<Projectile> projectiles = new Vector<>();
    Player player = new Player(500, 500, 0, 0, 0);

    boolean z_flag = false;
    boolean q_flag = false;
    boolean d_flag = false;
    boolean space_flag = false;

    long last_time;

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            //System.out.println(last_time + "    " + l + "   diff = " + (l-last_time)/1000000);
            last_time = l;
            try {
                update();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public paneltest(int width, int height){
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
        timer.start();
    }

    void update() throws InterruptedException {
        player.handle_inputs(z_flag, d_flag, q_flag);
        player.update();
        if (player.can_shoot() & space_flag){
            Projectile p = player.shoot();
            Platform.runLater(new addother<>(p, projectiles));
            Platform.runLater(new additem(p.get_sprite(), getChildren()));
        }
        Iterator<Projectile> p_it = projectiles.iterator();
        Projectile p;
        while (p_it.hasNext()){
            p = p_it.next();
            p.update();
            if (p.is_dead()){
                removeSprite(p.get_sprite());
                p_it.remove();
            }
            else{
                Iterator<Meteor> m_it = enemy.iterator();
                Meteor meteor;
                while (m_it.hasNext()){
                    meteor = m_it.next();
                    if (p.is_collision(meteor)){
                        remove_meteor(m_it, meteor);
                        removeSprite(p.get_sprite());
                        p_it.remove();
                    }
                }
            }

        }
        Iterator<Meteor> m_it = enemy.iterator();
        Meteor meteor;
        while (m_it.hasNext()){
            meteor = m_it.next();
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


    private void removeSprite(Polygon sprite){
        Iterator<Node> children = getChildren().iterator();
        Node next;
        while (children.hasNext()){     //finds and safely deletes the node
            next = children.next();
            if (next == sprite){
                children.remove();
            }
        }
    }


    private void remove_meteor(Iterator<Meteor> m_it, Meteor meteor){
        removeSprite(meteor.get_sprite());
        m_it.remove();
        int new_size = meteor.get_size()-1;
        if (new_size > 0){
            Meteor baby = meteor.break_it();
            Platform.runLater(new addother<>(baby, enemy));
            Platform.runLater(new additem(baby.get_sprite(), getChildren()));
            baby = meteor.break_it();
            Platform.runLater(new addother<>(baby, enemy));
            Platform.runLater(new additem(baby.get_sprite(), getChildren()));
        }
    }




}
class addother<K extends FloatingItems> implements Runnable{

    K item;
    Vector<K> v;

    addother(K item, Vector<K> v){
        this.item = item;
        this.v = v;
    }


    @Override
    public void run() {
        v.add(item);
    }
}