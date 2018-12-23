package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

public class StandingLocation {

    Point3D coordinate;
    long nanoBotsWithinReach;
    public StandingLocation(Point3D coordinate, long nanoBotsWithinReach){
        this.coordinate = coordinate;
        this.nanoBotsWithinReach = nanoBotsWithinReach;
    }

    public Point3D getCoordinate() {
        return coordinate;
    }

    public long getNanoBotsWithinReach() {
        return nanoBotsWithinReach;
    }


    public int manhattandistanceTo(Point3D other){
        return (int)(Math.abs(other.getX() - this.coordinate.getX()) +
                Math.abs(other.getY() - this.coordinate.getY()) +
                Math.abs(other.getZ() - this.coordinate.getZ()));
    }
}

