package model.asteroid;

import javafx.scene.shape.Polygon;

public class FloatingItems {

    float x = 0;
    float y = 0;
    float x_speed = 0;
    float y_speed = 0;
    float angle = 0;
    Polygon sprite;

    FloatingItems(){};

    FloatingItems(float x, float y, float x_speed, float y_speed, float angle){
        this.x = x;
        this.y = y;
        this.x_speed = x_speed;
        this.y_speed = y_speed;
        this.angle = angle;
    }

    void update(){
        x+=x_speed;
        y+=y_speed;
        sprite.setTranslateX(x);
        sprite.setTranslateY(y);
    }

    public float get_x(){
        return x;
    }

    public Polygon get_sprite(){
        return sprite;
    }
}
