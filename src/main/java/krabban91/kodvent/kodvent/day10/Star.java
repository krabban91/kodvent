package krabban91.kodvent.kodvent.day10;

import java.awt.*;

public class Star {

    private int x;
    private int y;
    private int dx;
    private int dy;

    public Star(String line){
        String[] split = line.split("<");
        String[] split1 = split[1].split(",");
        x = Integer.parseInt(split1[0].replace(" ",""));
        y = Integer.parseInt(split1[1].split(">")[0].replace(" ",""));
        String[] split2 = split[2].split(",");
        dx = Integer.parseInt(split2[0].replace(" ",""));
        dy = Integer.parseInt(split2[1].split(">")[0].replace(" ",""));
    }

    public boolean isWithinManhattanDistanceOf(Star other, int distance){
        return distance >= (Math.abs(this.getX() - other.getX()) + Math.abs(this.getY() - other.getY()));
    }

    public boolean hasXValue(int value){
        return x == value;
    }

    public void moveOneSecond(){
        x +=dx;
        y +=dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
