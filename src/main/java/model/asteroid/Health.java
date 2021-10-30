package model.asteroid;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

public class Health {

    int act_health;
    int max_health;
    Polygon[] sprites;
    int pos_x = 5;
    int pos_y = 40;


    public Health(Polygon sprite, int max_health, ObservableList<Node> display){
        this.max_health = max_health;
        act_health = max_health;
        sprites = new Polygon[max_health];
        for (int i = 0; i < max_health; i++){
            sprites[i] = new Polygon();
            sprites[i].getPoints().addAll(sprite.getPoints());
            sprites[i].setFill(Paint.valueOf("transparent"));
            sprites[i].setStroke(Paint.valueOf("white"));
            display.add(sprites[i]);
            sprites[i].setTranslateX(pos_x + i*15);
            sprites[i].setTranslateY(pos_y);
            sprites[i].setRotate(90);
        }
    }

    public boolean lose_health(){
        if (act_health > 0){
            act_health--;
            sprites[act_health].setVisible(false);
        }
        return act_health > 0;
    }


}
