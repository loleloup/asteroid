package model.asteroid;

import javafx.scene.shape.Polygon;

public class FloatingItems {

    static float wind_width;
    static float wind_height;
    static float Hrat = 1;
    static float Vrat = 1;


    float x = 0;
    float y = 0;
    float x_speed = 0;
    float y_speed = 0;
    float angle = 0;
    Polygon sprite;

    FloatingItems(){}

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

        sprite.setTranslateX(x*Hrat);
        sprite.setTranslateY(y*Vrat);
        sprite.setRotate(angle);
    }

    public static void set_wind_size(float width, float height){
        wind_width = width;
        wind_height = height;
    }


    public boolean is_collision(FloatingItems other){
        return sprite.intersects(sprite.sceneToLocal(other.sprite.localToScene(other.sprite.getBoundsInLocal())));
    }

    public void set_position(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void set_speed(float x_speed, float y_speed){
        this.x_speed = x_speed;
        this.y_speed = y_speed;
    }

    public Polygon get_sprite(){
        return sprite;
    }

    public static float getWind_width(){
        return wind_width;
    }

    public static float getWind_height(){
        return wind_height;
    }

    public void scale_all(float Hrat, float Vrat){
        sprite.setScaleX(Hrat);
        sprite.setScaleY(Vrat);
    }


    public static void new_rat(float Hrat, float Vrat){
        FloatingItems.Hrat *= Hrat;
        FloatingItems.Vrat *= Vrat;
    }
}
