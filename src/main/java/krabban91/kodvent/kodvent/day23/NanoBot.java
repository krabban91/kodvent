package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

public class NanoBot {

    int signalRadius;
    Point3D coordinate;
    public NanoBot(Point3D point, int radius){
        this.coordinate = point;
        this.signalRadius = radius;
    }
    public NanoBot(String input){
        String[] split = input.split(">, r=");
        String substring = split[0].substring(5);
        String[] split1 = substring.split(",");
        coordinate = new Point3D(Integer.parseInt(split1[0]),Integer.parseInt(split1[1]),Integer.parseInt(split1[2]));
        signalRadius = Integer.parseInt(split[1]);
    }

    public int manhattandistanceTo(NanoBot other){
        return manhattandistanceTo(other.coordinate);
    }

    public int manhattandistanceTo(Point3D other){
        return Math.abs((int) other.getX() - this.getX()) +
                Math.abs((int) other.getY() - this.getY()) +
                Math.abs((int) other.getZ() - this.getZ());
    }

    public boolean isWithinRange(Point3D other) {
        return this.manhattandistanceTo(other) <= this.getSignalRadius();
    }

    public int getSignalRadius() {
        return signalRadius;
    }

    public int getX(){
        return (int)coordinate.getX();
    }

    public int getY(){
        return (int)coordinate.getY();
    }
    public int getZ(){
        return (int)coordinate.getZ();
    }
}
