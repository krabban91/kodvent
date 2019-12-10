package krabban91.kodvent.kodvent.y2019.d10;

import java.awt.*;

public class AsteroidSlot {
    private final Point point;
    private boolean isAsteroid;

    public AsteroidSlot(int x, int y, boolean isAsteroid){
        this.isAsteroid = isAsteroid;
        this.point = new Point(x,y);
    }

    public Point getPoint() {
        return point;
    }

    public boolean isAsteroid() {
        return isAsteroid;
    }

    public void obliterate(){
        this.isAsteroid = false;
    }
}
