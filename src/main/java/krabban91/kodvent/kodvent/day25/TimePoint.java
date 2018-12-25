package krabban91.kodvent.kodvent.day25;

import krabban91.kodvent.kodvent.day15.CaveBattle;

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
        return manhattanDistance(this.xy,other.xy) + manhattanDistance(this.zt, other.zt);
    }

    private static int manhattanDistance(Point a, Point b){
        return CaveBattle.manhattanDistance(a,b);
    }
}
