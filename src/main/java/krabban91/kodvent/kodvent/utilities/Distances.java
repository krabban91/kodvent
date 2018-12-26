package krabban91.kodvent.kodvent.utilities;

import java.awt.*;

public class Distances {

    public static int delta(int i1, int i2){
        return Math.abs(i1-i2);
    }
    public static int manhattan(Point p1, Point p2){
        return delta(p1.x,p2.x)+delta(p1.y,p2.y);
    }

    public static int manhattan(Point3D p1, Point3D p2){
        return delta(p1.getX(),p2.getX())+delta(p1.getY(), p2.getY())+ delta(p1.getZ(),p2.getZ());
    }
    public static int manhattan(TimePoint p1, TimePoint p2){
        return manhattan(p1.getXy(), p2.getXy()) + manhattan(p1.getZt(),p2.getZt());
    }
}
