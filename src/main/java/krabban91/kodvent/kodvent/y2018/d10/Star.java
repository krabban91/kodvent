package krabban91.kodvent.kodvent.y2018.d10;

import java.awt.*;

public class Star {

    private Point position;
    private Point velocity;

    public Star(String line){
        String[] split = line.split("<");
        String[] split1 = split[1].split(",");
        position = new Point(
                Integer.parseInt(split1[0].replace(" ", "")),
                Integer.parseInt(split1[1].split(">")[0].replace(" ", "")));
        String[] split2 = split[2].split(",");
        velocity = new Point(
                Integer.parseInt(split2[0].replace(" ", "")),
                Integer.parseInt(split2[1].split(">")[0].replace(" ", "")));
    }

    public boolean isWithinManhattanDistanceOf(Star other, int distance){
        return distance >= (Math.abs(this.getX() - other.getX()) + Math.abs(this.getY() - other.getY()));
    }

    public boolean hasXValue(int value){
        return position.x == value;
    }

    public void moveOneSecond(){
        position.translate(velocity.x, velocity.y);
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

}
