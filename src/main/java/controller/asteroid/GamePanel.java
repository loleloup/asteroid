package controller.asteroid;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.asteroid.FloatingItems;
import model.asteroid.Meteor;
import model.asteroid.Player;
import model.asteroid.Projectile;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class GamePanel extends AnchorPane {

    Vector<Meteor> enemy = new Vector<>();
    Vector<Projectile> projectiles = new Vector<>();
    Player player = new Player(500, 500, 0, 0, 0);

    boolean z_flag = false;
    boolean q_flag = false;
    boolean d_flag = false;
    boolean space_flag = false;

    //avoid repetitive calls to getChildren()
    ObservableList<Node> children;
    ObservableList<Node> meteor_sprites;
    ObservableList<Node> projectile_sprites;

    Label score = new Label("0");

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

    public GamePanel(Stage primaryStage, float width, float height){
        super();
        FloatingItems.set_wind_size(width, height);

        Group meteor_group = new Group();
        Group proj_group = new Group();

        children = getChildren();
        meteor_sprites = meteor_group.getChildren();
        projectile_sprites = proj_group.getChildren();

        children.add(meteor_group);
        children.add(proj_group);

        children.add(score);

// create a listener
        final ChangeListener<Number> listener = new ChangeListener<Number>()
        {
            final Timer timer = new Timer(); // uses a timer to call your resize method
            TimerTask task = null; // task to execute after defined delay
            final long delayTime = 200; // delay that has to pass in order to consider an operation done

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue)
            {
                if (task != null)
                { // there was already a task scheduled from the previous operation ...
                    task.cancel(); // cancel it, we have a new size to consider
                }

                task = new TimerTask() // create new task that calls your resize operation
                {
                    @Override
                    public void run()
                    {
                        System.out.println("resize to " + primaryStage.getWidth() + " " + primaryStage.getHeight());
                        // here you can place your resize code
                        float old_width = FloatingItems.getWind_width();
                        float old_height = FloatingItems.getWind_height();
                        float width = (float)primaryStage.getWidth();
                        float height = (float)primaryStage.getHeight();

                        float Hrat = width/old_width;
                        float Vrat = height/old_height;

                        FloatingItems.set_wind_size(width, height);

                        for (Projectile p : projectiles){
                            p.scale_all(Hrat, Vrat);
                        }
                        for (Meteor meteor : enemy){
                            meteor.scale_all(Hrat, Vrat);
                        }
                        player.scale_all(Hrat, Vrat);

                    }
                };
                // schedule new task
                timer.schedule(task, delayTime);
            }
        };

// finally we have to register the listener
        primaryStage.widthProperty().addListener(listener);
        primaryStage.heightProperty().addListener(listener);
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
        //children.add(m.get_sprite());
        meteor_sprites.add(m.get_sprite());
        children.add(player.get_sprite());
        setTopAnchor(player.get_sprite(), 0.0);
        setBottomAnchor(player.get_sprite(), 720.0);
        setLeftAnchor(player.get_sprite(), 0.0);
        setRightAnchor(player.get_sprite(), 1080.0);
        timerThread.start();
    }

    void update() {
        player.handle_inputs(z_flag, d_flag, q_flag);
        player.update();
        if (player.can_shoot() & space_flag){
            Platform.runLater(this::player_shoot);
        }
        Iterator<Projectile> p_it = projectiles.iterator();
        Projectile p;
        while (p_it.hasNext()){
            p = p_it.next();
            p.update();
            if (p.is_dead()){
                removeSprite(p.get_sprite(), projectile_sprites);
                p_it.remove();
            }
            else{
                Iterator<Meteor> m_it = enemy.iterator();
                Meteor meteor;
                while (m_it.hasNext()){
                    meteor = m_it.next();
                    if (p.is_collision(meteor)){
                        p_it.remove();
                        Platform.runLater(new removemeteor(meteor));
                        m_it.remove();
                        removeSprite(p.get_sprite(), projectile_sprites);
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
            }
        }
    }


    private void player_shoot(){
        Projectile p = player.shoot();
        projectiles.add(p);
        projectile_sprites.add(p.get_sprite());
    }


    private void removeSprite(Polygon sprite, ObservableList<Node> group){
        Iterator<Node> child_it = group.iterator();
        Node next;
        while (child_it.hasNext()){     //finds and safely deletes the node
            next = child_it.next();
            if (next == sprite){
                child_it.remove();
            }
        }
    }


    class removemeteor implements Runnable{

        Meteor meteor;

        removemeteor(Meteor meteor){
            this.meteor = meteor;
        }

        @Override
        public void run() {
            meteor_sprites.remove(meteor.get_sprite());
            int new_size = meteor.get_size()-1;
            if (new_size > 0){
                Meteor baby = meteor.break_it();
                enemy.add(baby);
                meteor_sprites.add(baby.get_sprite());
                baby = meteor.break_it();
                enemy.add(baby);
                meteor_sprites.add(baby.get_sprite());
            }
        }
    }
}
