package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StandingLocation {

    Point3D coordinate;
    long nanoBotsWithinReach;
    Set<NanoBot> reaches;
    public StandingLocation(Point3D coordinate, long nanoBotsWithinReach){
        this.coordinate = coordinate;
        this.nanoBotsWithinReach = nanoBotsWithinReach;
    }

    public StandingLocation(Point3D coordinate, Set<NanoBot> reaches){
        this.coordinate = coordinate;
        this.nanoBotsWithinReach = reaches.size();
        this.reaches= reaches;
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

    public Set<Point3D> getLocationsWithinDistance(int distance){
        return IntStream.rangeClosed(getZ()-distance, getZ()+distance)
                .mapToObj(z -> {
                    int yLimits = distance-Math.abs(z - getZ());
                    return IntStream.rangeClosed(getY()-yLimits, getY()+yLimits)
                            .mapToObj(y -> {
                                int xLimits = yLimits-Math.abs(y - getY());
                                return IntStream.rangeClosed(getX()-xLimits, getX()+xLimits)
                                        .mapToObj(x -> new Point3D(x,y,z)).collect(Collectors.toSet());
                            }).flatMap(Set::stream).collect(Collectors.toSet());
                }).flatMap(Set::stream).collect(Collectors.toSet());
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

