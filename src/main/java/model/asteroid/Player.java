package model.asteroid;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;

public class Player extends FloatingItems{

    double THRUSTER_POWER = 0.2;
    float angle_speed = 6;
    int alive = 0;
    int fire_rate = 15;
    int fire_delay = 0;
    Health health;

    public Player(float x, float y, ObservableList<Node> display) {
        super(x, y, 0, 0, 0);

        Double[] pointlist = new Double[]{0.0, 0.0, 15.0, -7.0, 10.0, 0.0, 15.0, 7.0};

        sprite.getPoints().addAll(pointlist);
        sprite.setTranslateX(x*Hrat);
        sprite.setTranslateY(y*Vrat);
        sprite.setScaleX(Hrat);
        sprite.setScaleY(Vrat);

        health = new Health(sprite, 3, display);

    }

    public void thrust(){
        x_speed -= Math.cos(Math.toRadians(angle)) * THRUSTER_POWER;
        y_speed -= Math.sin(Math.toRadians(angle)) * THRUSTER_POWER;
    }

    public void handle_inputs(boolean z_flag, boolean d_flag, boolean q_flag) {
        if (z_flag){
            thrust();
        }
        int rotate = 0;
        if (d_flag){
            rotate++;
        }
        if (q_flag){
            rotate--;
        }
        angle += rotate * angle_speed;
    }

    public boolean lose_life(){
        alive = 180;    //3 sec invincibility
        return health.lose_health();
    }

    public void update(){
        super.update();
        if (alive > 0){
            alive--;
        }
        if (fire_delay > 0){
            fire_delay--;
        }
    }

    public boolean is_alive() {
        return alive == 0;
    }

    public boolean can_shoot(){
        return fire_delay == 0;
    }

    public Projectile shoot(){
        fire_delay = fire_rate;
        return new Projectile(x+6, y, (float) -Math.cos(Math.toRadians(angle)), -(float) Math.sin(Math.toRadians(angle)));
    }


    public void reset_pos(){
        x = wind_width/2;
        y = wind_height/2;
    }

    //TODO add hyperspace TP
}
