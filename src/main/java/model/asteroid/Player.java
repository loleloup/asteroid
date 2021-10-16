package model.asteroid;

public class Player extends FloatingItems{

    double THRUSTER_POWER = 0.2;
    int MAX_SPEED = 10;
    int angle_deg;

    Player(float x, float y, float x_speed, float y_speed, int angle_deg) {
        super(x, y, x_speed, y_speed);
        this.angle_deg = angle_deg;
    }

    void thrust(float v_x, float v_y){
        x_speed += v_x * THRUSTER_POWER;
        y_speed += v_y * THRUSTER_POWER;
    }

}
