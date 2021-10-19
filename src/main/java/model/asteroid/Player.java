package model.asteroid;

import javafx.scene.shape.Polygon;

public class Player extends FloatingItems{

    double THRUSTER_POWER = 0.3;
    float angle_speed = 5;
    int MAX_SPEED = 10;
    int lifes = 0;

    public Player(float x, float y, float x_speed, float y_speed, int angle_deg) {
        super(x, y, x_speed, y_speed, 0);

        Double[] pointlist = new Double[]{0.0, 0.0, 15.0, -7.0, 10.0, 0.0, 15.0, 7.0};

        sprite = new Polygon();
        sprite.getPoints().addAll(pointlist);
        sprite.setTranslateX(x);
        sprite.setTranslateY(y);
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
}
