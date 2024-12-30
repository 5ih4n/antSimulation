package org.evensen.ants;
import java.util.Random;

public class MyAntWorld implements AntWorld {
    public float[][] foragingPheromones;
    public float[][] foodPheromones;
    boolean[][] foods;
    DispersalPolicy dp;
    Random rand = new Random();

    FoodSource[] foodSources = {
            new FoodSource(new Position(this.rand.nextInt(400), this.rand.nextInt(200))),
            new FoodSource(new Position(this.rand.nextInt(400), this.rand.nextInt(200))),
            new FoodSource(new Position(this.rand.nextInt(400), this.rand.nextInt(200))),
            new FoodSource(new Position(this.rand.nextInt(400), this.rand.nextInt(200))),
    };

    public MyAntWorld(final int worldWidth, final int worldHeight, final int i,DispersalPolicy dp) {
        this.foragingPheromones = new float[worldWidth + 1][worldHeight + 1];
        this.foodPheromones = new float[worldWidth + 1][worldHeight + 1];
        this.foods = new boolean[worldWidth + 1][ worldHeight + 1];
        this.dp = dp;
        updateFoods();
    }

    public void updateFoods(){
        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                this.foods[i][j] = containsFood2(new Position(i,j));
            }
        }
    }

    public boolean containsFood2(final Position p) {
        for (final FoodSource foodSource : this.foodSources) {
            if (foodSource.containsFood(p)){
                return true;
            }
        }
        return false;
    }


    /**
     * @return
     */
    @Override
    public int getWidth() {
        return 400;
    }

    /**
     * @return
     */
    @Override
    public int getHeight() {
        return 200;
    }

    /**
     * @param
     * @return
     */
    @Override
    public boolean isObstacle(final Position p) {
        if (0 > p.getX() || 200 < p.getY() || 0 > p.getY() || 400 < p.getX()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param p      The position where the pheromone should be dropped.
     * @param amount The amount to be dropped. Note that this *increases* rather than sets the current level.
     */
    @Override
    public void dropForagingPheromone(final Position p, final float amount) {
        if(this.foragingPheromones[(int) p.getX()][(int) p.getY()] + amount > 1){
            this.foragingPheromones[(int) p.getX()][(int) p.getY()] = 1;
        } else {
            this.foragingPheromones[(int) p.getX()][(int) p.getY()] += amount;
        }
    }


    /**
     * @param p      The position where the pheromone should be dropped.
     * @param amount The amount to be dropped. Note that this *increases* rather than sets the current level.
     */
    @Override
    public void dropFoodPheromone(final Position p, final float amount) {
        if(this.foodPheromones[(int) p.getX()][(int) p.getY()] + amount > 1){
            this.foodPheromones[(int) p.getX()][(int) p.getY()] = 1;
        } else {
            this.foodPheromones[(int) p.getX()][(int) p.getY()] += amount;
        }
    }

    /**
     * @param p The position to drop food at.
     */
    @Override
    public void dropFood(final Position p) {

    }

    /**
     * @param p The position to pick food from.
     */
    @Override
    public void pickUpFood(final Position p) {
        //System.out.println(p.getX() + " " + p.getY());
        for(int i = 0; i < this.foodSources.length; i++){
            if(p.isWithinRadius(this.foodSources[i].p, this.foodSources[i].radius + 1)){
                this.foodSources[i].takeFood();
                if (0 == this.foodSources[i].mouths){
                    this.foodSources[i].respwn();
                    updateFoods();
                }
                return;
            }
        }
    }

    /**
     * @param p
     * @return
     */
    @Override
    public float getDeadAntCount(final Position p) {
        return 0;
    }

    /**
     * @param p The position to get the foraging pheromone level for.
     * @return
     */
    @Override
    public float getForagingStrength(final Position p) {
       return this.foragingPheromones[(int) p.getX()][(int) p.getY()];
    }

    /**
     * @param p The position to get the food pheromone level for.
     * @return
     */
    @Override
    public float getFoodStrength(final Position p) {
        return this.foodPheromones[(int) p.getX()][(int) p.getY()];
    }

    /**
     * @param p The position to check for food.
     * @return
     */
    @Override
    public boolean containsFood(final Position p) {
        return this.foods[Math.round(p.getX())][Math.round(p.getY())];
    }


    /**
     * @return
     */
    @Override
    public long getFoodCount() {
        return 0;
    }

    /**
     * @param p The position to check for homeness.
     * @return
     */
    @Override
    public boolean isHome(final Position p) {
        if(p.isWithinRadius(new Position(300,100), 10)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     */
    public void selfContainedDisperse(){
        for (FoodSource foodSource : this.foodSources) {
            dropFoodPheromone(foodSource.p, 1);
        }
    }

    @Override
    public void dispersePheromones() {
        selfContainedDisperse();

        float[][] temp = new float[getWidth() + 1][getHeight() + 1];
        float[][] temp1 = new float[getWidth() + 1][getHeight() + 1];

        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j< getHeight(); j++){
                Position p = new Position(i,j);
                float[] vals= this.dp.getDispersedValue(this, p);
                temp[i][j] = vals[0];
                temp1[i][j] = vals[1];
            }
        }

        this.foragingPheromones = temp;
        this.foodPheromones = temp1;
    }


    /**
     * @param p   The position to add/remove an obstacle to.
     * @param add If {@code true}, adds an obstacle at {@code p}, otherwise removes at {@code p}.
     */
    @Override
    public void setObstacle(final Position p, final boolean add) {
    }

    /**
     * @param p        The position to hit.
     * @param strength Could be used for anything.
     */
    @Override
    public void hitObstacle(final Position p, final float strength) {

    }
}
