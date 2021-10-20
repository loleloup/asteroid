package model.asteroid;

import javafx.scene.shape.Polygon;

public class FloatingItems {

    static int wind_width;
    static int wind_height;


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

    public void update(){
        x+=x_speed;
        y+=y_speed;
        if (x > wind_width){
            x = x%wind_width;
        }
        else if (x < 0){
            x += wind_width;
        }
        if (y < 0){
            y += wind_height;
        }
        else if (y > 0){
            y = y%wind_height;
        }

        sprite.setTranslateX(x);
        sprite.setTranslateY(y);
        sprite.setRotate(angle);
    }

    public static void set_wind_size(int width, int height){
        wind_width = width;
        wind_height = height;
    }

    public float get_x(){
        return x;
    }

    public float get_angle(){
        return angle;
    }

    public Polygon get_sprite(){
        return sprite;
    }
}
