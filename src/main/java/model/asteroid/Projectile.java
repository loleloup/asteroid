package model.asteroid;

import javafx.scene.shape.Polygon;

public class Projectile extends FloatingItems{

    static float vel = 5;
    int lifetime = 120;

    public Projectile(float x, float y, float x_speed, float y_speed){

        super(x, y, x_speed*vel, y_speed*vel, 0);


        Double[] points = new Double[]{1.0, 0.0, 0.0, 1.0, -1.0, 0.0, 0.0, -1.0};

        sprite = new Polygon();
        sprite.getPoints().addAll(points);


    }

    public void update(){
        super.update();
        lifetime--;
    }

    public boolean is_dead(){return lifetime == 0;}


}