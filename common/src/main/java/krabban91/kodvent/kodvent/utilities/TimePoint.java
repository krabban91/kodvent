package krabban91.kodvent.kodvent.utilities;

import java.awt.*;

public class TimePoint {

    Point xy;
    Point zt;


    public TimePoint(String input){
        String[] split = input.split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        int z = Integer.parseInt(split[2]);
        int t = Integer.parseInt(split[3]);
        this.xy = new Point(x,y);
        this.zt = new Point(z,t);
    }

    public boolean equals(Object other){
        if(other instanceof TimePoint){
            TimePoint point = (TimePoint)other;
            boolean xyEq = xy.equals(point.xy);
            if(xyEq){
                return zt.equals(point.zt);
            }
            return xyEq;
        }
        return false;
    }

    public int manhattanDistance(TimePoint other){
        return Distances.manhattan(this,other);
    }

    public Point getXy() {
        return xy;
    }

    public Point getZt() {
        return zt;
    }

}
