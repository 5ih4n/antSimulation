package org.evensen.ants;
import java.util.Random;


public class FoodSource {
    Position p;
    float radius = 5;
    int mouths = 5000;
    Random rand = new Random();

    public FoodSource(Position p) {
        this.p = p;
    }

    public void takeFood() {
        this.mouths -= 1;
    }

    public boolean containsFood(Position p2){
        if(p2.isWithinRadius(this.p, this.radius)){
            return true;
        } else{
            return false;
        }
    }

    public void respwn(){
        this.p  = new Position(this.rand.nextInt(400), this.rand.nextInt(200));
        this.mouths = 5000;
    }
}
