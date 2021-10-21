package model.asteroid;

import javafx.scene.Node;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Meteor extends FloatingItems{

    float rot_speed = 0;
    int size = 3;

    public Meteor(float x, float y, float x_speed, float y_speed, int size){
        super(x, y, x_speed, y_speed, 0);
        this.size = size;
        Random rand = new Random();
        int edges = rand.nextInt(5) + 7;    //minimum a pentagon, maximum a decagon
        int semi_diag = rand.nextInt(15)+30;
        Double[] pointlist = new Double[edges*2];
        for (int i = 0; i < edges; i++){
            double uncertainty_x = (rand.nextDouble() - 0.5)*20;
            double uncertainty_y = (rand.nextDouble() - 0.5)*20;
            pointlist[i*2] = ((Math.cos(Math.toRadians(360.0/edges*i))*semi_diag)+uncertainty_x)*(0.1+0.4*size);
            pointlist[i*2+1] = ((Math.sin(Math.toRadians(360.0/edges*i))*semi_diag)+uncertainty_y)*(0.1+0.4*size);
        }
        sprite = new Polygon();
        sprite.getPoints().addAll(pointlist);

        rot_speed = rand.nextInt(4)+1;
    }


    public Meteor break_it(){
        Random rand = new Random();
        return new Meteor(x, y, (float)(x_speed + (rand.nextFloat()-0.5)*10), (float)(y_speed + (rand.nextFloat()-0.5)*10), size-1);
    }

    public void update(){
        super.update();
        angle += rot_speed;
        sprite.setRotate(angle);

    }

    public int get_size(){
        return size;
    }
}
