package model.asteroid;

import javafx.scene.Node;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Meteor extends FloatingItems{

    float rot_speed = 0;

    public Meteor(float x, float y, float x_speed, float y_speed){
        super(x, y, x_speed, y_speed, 0);
        Random rand = new Random();
        int edges = rand.nextInt(5) + 7;    //minimum a pentagon, maximum a decagon
        int semi_diag = rand.nextInt(30)+30;
        Double[] pointlist = new Double[edges*2];
        for (int i = 0; i < edges; i++){
            double uncertainty_x = (rand.nextDouble() - 0.5)*45;
            double uncertainty_y = (rand.nextDouble() - 0.5)*45;
            pointlist[i*2] = (Math.cos(Math.toRadians(360.0/edges*i))*semi_diag)+uncertainty_x;
            pointlist[i*2+1] = (Math.sin(Math.toRadians(360.0/edges*i))*semi_diag)+uncertainty_y;
        }
        sprite = new Polygon();
        sprite.getPoints().addAll(pointlist);

        rot_speed = rand.nextInt(4)+1;
    }


    public void update(){
        super.update();
        angle += rot_speed;
        sprite.setRotate(angle);

    }


}
