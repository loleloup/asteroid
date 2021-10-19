package model.asteroid;

import javafx.scene.shape.Polygon;

public class Player extends FloatingItems{

    double THRUSTER_POWER = 0.2;
    int MAX_SPEED = 10;
    int lifes = 0;

    public Player(float x, float y, float x_speed, float y_speed, int angle_deg) {
        super(x, y, x_speed, y_speed, 0);

        Double[] pointlist = new Double[]{0.0, 0.0, -7.0, 15.0, 0.0, 10.0, 7.0, 15.0};

        sprite = new Polygon();
        sprite.getPoints().addAll(pointlist);
        sprite.setTranslateX(x);
        sprite.setTranslateY(y);
    }

    void thrust(float v_x, float v_y){
        x_speed += v_x * THRUSTER_POWER;
        y_speed += v_y * THRUSTER_POWER;
    }

}
