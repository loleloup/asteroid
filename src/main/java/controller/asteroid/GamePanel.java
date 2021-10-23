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
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.asteroid.FloatingItems;
import model.asteroid.Meteor;
import model.asteroid.Player;
import model.asteroid.Projectile;

import java.util.*;

import static java.lang.Thread.sleep;


//TODO spread responsibilities with other classes?
public class GamePanel extends AnchorPane {

    Vector<Meteor> enemy = new Vector<>();
    Vector<Projectile> projectiles = new Vector<>();
    Player player = new Player(500, 500, 0, 0);

    //for responsive player inputs
    boolean z_flag = false;
    boolean q_flag = false;
    boolean d_flag = false;
    boolean space_flag = false;

    //avoid repetitive calls to getChildren()
    ObservableList<Node> children;
    ObservableList<Node> meteor_sprites;
    ObservableList<Node> projectile_sprites;

    float old_width;
    float old_height;

    //score handling
    int score = 0;
    Label score_label = new Label("0");

    //new wave signal
    boolean new_wave = true;
    int wave_nb = 0;

    //game refresh timer 60Hz
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
        this.old_width = width;
        this.old_height = height;

        FloatingItems.set_wind_size(width, height);

        Group meteor_group = new Group();
        Group proj_group = new Group();

        children = getChildren();
        meteor_sprites = meteor_group.getChildren();
        projectile_sprites = proj_group.getChildren();

        children.add(meteor_group);
        children.add(proj_group);

        children.add(score_label);


        final ChangeListener<Number> listener = getChangeListener(primaryStage);

// finally we have to register the listener
        primaryStage.widthProperty().addListener(listener);
        primaryStage.heightProperty().addListener(listener);
    }

    private ChangeListener<Number> getChangeListener(Stage primaryStage) {
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
                        //System.out.println("resize to " + primaryStage.getWidth() + " " + primaryStage.getHeight());

                        float width = (float) primaryStage.getWidth();
                        float height = (float) primaryStage.getHeight();

                        float Hrat = width/old_width;
                        float Vrat = height/old_height;

                        old_width = width;
                        old_height = height;

                        //FloatingItems.set_wind_size(width, height);
                        FloatingItems.new_rat(Hrat, Vrat);

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
        return listener;
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

        children.add(player.get_sprite());
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
            if (p.is_dead()){   //projectile's lifetime ended
                removeSprite(p.get_sprite(), projectile_sprites);
                p_it.remove();
            }
            else{   //check for collision with meteors
                Iterator<Meteor> m_it = enemy.iterator();
                Meteor meteor;
                while (m_it.hasNext()){
                    meteor = m_it.next();
                    if (p.is_collision(meteor)){    //remove projectile and meteor (+breaks the meteor)
                        p_it.remove();
                        Platform.runLater(new removemeteor(meteor));
                        m_it.remove();
                        removeSprite(p.get_sprite(), projectile_sprites);
                        if (enemy.isEmpty()){   //destroyed last meteor
                            new_wave = true;
                        }
                    }
                }
            }

        }
        Iterator<Meteor> m_it = enemy.iterator();
        Meteor meteor;
        while (m_it.hasNext()){     //updates meteors
            meteor = m_it.next();
            meteor.update();
            if (player.is_alive() & meteor.is_collision(player)){   //check collision with player
                if (!player.lose_life()){
                    //gameover
                }
                else{

                }
            }
        }

        if (new_wave){
            wave_nb++;
            new_wave = false;
            create_wave();
        }
    }

    private void create_wave() {
        Random rand = new Random();
        int meteor_nb = wave_nb * 2;
        Meteor new_m;
        for (int i = 0; i < meteor_nb/2; i++){
            new_m = new Meteor(rand.nextInt((int) FloatingItems.getWind_width()), 0, (rand.nextFloat()-0.5)*2, (rand.nextFloat()-0.5)*2, 3);
            enemy.add(new_m);
            meteor_sprites.add(new_m.get_sprite());
            new_m = new Meteor(0, rand.nextInt( (int) FloatingItems.getWind_height()), (rand.nextFloat()-0.5)*2, (rand.nextFloat()-0.5)*2, 3);
            enemy.add(new_m);
            meteor_sprites.add(new_m.get_sprite());
        }

        projectiles.clear();
        projectile_sprites.clear();

        player.reset_pos();


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
            score += (new_size+1) * 10;
            score_label.setText(String.valueOf(score));
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
