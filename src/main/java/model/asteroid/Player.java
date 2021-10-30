package model.asteroid;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public class Player extends FloatingItems{

    double THRUSTER_POWER = 0.2;
    float angle_speed = 6;
    int alive = 0;
    int fire_rate = 15;
    int fire_delay = 0;
    Health health;
    boolean blink = true;

    public Player(float x, float y, ObservableList<Node> display) {
        super(x, y, 0, 0, 90);

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
        if (z_flag || d_flag || q_flag){    //deactivate invincibility if action pressed
            alive = 0;
            sprite.setVisible(true);
        }
        if (is_alive()){
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
    }

    public boolean lose_life(){
        alive = 180;    //3 sec invincibility
        reset();
        return health.lose_health();
    }

    public void update(){
        super.update();
        if (alive > 0){
            alive--;
            if (blink && alive%10 == 0){
                sprite.setVisible(false);
                blink = !blink;
            }
            else if (alive%10 == 0){
                sprite.setVisible(true);
                blink = !blink;
            }
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


    public void reset(){
        x = wind_width/2;
        y = wind_height/2;
        x_speed = 0;
        y_speed = 0;
        angle = 90;
    }

    //TODO add hyperspace TP
}
