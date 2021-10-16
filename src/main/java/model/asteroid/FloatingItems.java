package model.asteroid;

public class FloatingItems {

    float x;
    float y;
    float x_speed;
    float y_speed;

    FloatingItems(float x, float y, float x_speed, float y_speed){
        this.x = x;
        this.y = y;
        this.x_speed = x_speed;
        this.y_speed = y_speed;
    }

    void update(){
        x+=x_speed;
        y+=y_speed;
    }
}
