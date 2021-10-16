package model.asteroid;

public class Vector2D {

    double x = 0;
    double y = 0;

    Vector2D(){}
    Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    void add(Vector2D vect){
        x += vect.x;
        y += vect.y;
    }

    double get_v_angle(){
        /*
        get the angle between the vector and the vertical vector (0, 1)
         */

        return Math.toDegrees(Math.acos(y / Math.sqrt(x*x + y*y)));
    }

}
