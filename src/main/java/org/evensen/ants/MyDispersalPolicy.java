package org.evensen.ants;

import java.util.ArrayList;

public class MyDispersalPolicy implements DispersalPolicy{


    /**
     * @param w The world to extract pheromone levels from.
     * @param p The position to get levels from.
     * @return
     */
    @Override
    public float[] getDispersedValue(final AntWorld w, final Position p) {

        float k = 0.5f;
        float f = 0.95f;

        int x = (int) p.getX();
        int y = (int) p.getY();

        float[] npl = new float[2];
        if(!w.isObstacle(new Position(x,y))) {
            int left = Math.max(0, x-1);
            int right = Math.min(w.getWidth(),x+1);
            int bot = Math.min(w.getHeight(), y+1);
            int top = Math.max(0, y-1);

            ArrayList<Position> positions = new ArrayList<>();
            positions.add(new Position(left,top)) ;
            positions.add(new Position(left,y));
            positions.add(new Position(left,bot));
            positions.add(new Position(x,top));
            positions.add(new Position(x,bot));
            positions.add(new Position(right,top));
            positions.add(new Position(right,y));
            positions.add(new Position(right,bot));

            Position mm = new Position(x,y);

            float temp1 = 0;

            for(Position position : positions){
                temp1 += w.getForagingStrength(position);
            }

            float temp2 = 0;

            for(Position position : positions){
                temp2 += w.getFoodStrength(position);
            }

            npl[0] =  f * ((1-k) * temp1 / 8 + (k * w.getForagingStrength(mm)));
            npl[1] =  f * ((1-k) * temp2 / 8 + (k * w.getFoodStrength(mm)));

        }

        return npl;
    }


}
