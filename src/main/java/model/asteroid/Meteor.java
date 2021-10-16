package model.asteroid;

import java.util.Random;

public class Meteor extends FloatingItems{

    Double[] pointlist;

    public Meteor(float x, float y, float x_speed, float y_speed){
        super(x, y, x_speed, y_speed);
        Random rand = new Random();
        int edges = rand.nextInt(5) + 5;    //minimum a pentagon, maximum a decagon
        int semi_diag = rand.nextInt(30)+30;
        pointlist = new Double[edges*2];
        System.out.println(edges);
        for (int i = 0; i < edges; i++){
            double uncertainty_x = (rand.nextDouble() - 0.5)*30;
            double uncertainty_y = (rand.nextDouble() - 0.5)*30;
            pointlist[i*2] = (Math.cos(Math.toRadians(360.0/edges*i))*semi_diag)+uncertainty_x;
            pointlist[i*2+1] = (Math.sin(Math.toRadians(360.0/edges*i))*semi_diag)+uncertainty_y;
        }
    }

    public Double[] get_points(){
        return pointlist;
    }
}
