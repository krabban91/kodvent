package krabban91.kodvent.kodvent.utilities;

import java.awt.*;
import java.sql.Time;

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

    public TimePoint(int x,int y,int z,int w){
        this.xy = new Point(x,y);
        this.zt = new Point(z,w);
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

    public int getX() {
        return xy.x;
    }
    public int getY() {
        return xy.y;
    }
    public int getZ() {
        return zt.x;
    }
    public int getW() {
        return zt.y;
    }

    public TimePoint add(TimePoint other) {
        return new TimePoint(this.getX() + other.getX(),
                this.getY() + other.getY(),
                this.getZ() + other.getZ(),
                this.getW() + other.getW());
    }


}
