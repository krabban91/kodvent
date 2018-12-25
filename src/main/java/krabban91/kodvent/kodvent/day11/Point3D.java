package krabban91.kodvent.kodvent.day11;

import krabban91.kodvent.kodvent.day15.CaveBattle;

import java.awt.*;

public class Point3D {

    Point xy;
    int z;


    public Point3D(int x, int y, int z){
        this.xy = new Point(x,y);
        this.z = z;
    }

    public boolean equals(Object other){
        if(other instanceof Point3D){
            Point3D point = (Point3D)other;
            boolean xyEq = xy.equals(point.xy);
            if(xyEq){
                return this.z == point.z;
            }
            return xyEq;
        }
        return false;
    }

    public int manhattanDistance(Point3D other){
        return manhattanDistance(this.xy,other.xy) + Math.abs(this.z - other.z);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Position<");
        builder.append(xy.x);
        builder.append(",");
        builder.append(xy.y);
        builder.append(",");
        builder.append(z);
        builder.append(">");
        return builder.toString();
    }

    public int getX(){
        return this.xy.x;
    }
    public int getY(){
        return this.xy.y;
    }
    public int getZ(){
        return this.z;
    }

    private static int manhattanDistance(Point a, Point b){
        return CaveBattle.manhattanDistance(a,b);
    }

}
