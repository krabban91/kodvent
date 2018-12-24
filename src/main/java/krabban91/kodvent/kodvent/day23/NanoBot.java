package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NanoBot {

    int signalRadius;
    Point3D coordinate;
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
        return Math.abs(SearchGrid.getX(other) - this.getX()) +
                Math.abs(SearchGrid.getY(other) - this.getY()) +
                Math.abs(SearchGrid.getZ(other) - this.getZ());
    }

    public boolean isWithinRange(Point3D other) {
        return this.manhattandistanceTo(other) >= this.getSignalRadius();
    }

    public int getSignalRadius() {
        return signalRadius;
    }

    public Set<Point3D> getLocationsWithinReach(){
        return IntStream.rangeClosed(getZ()-signalRadius, getZ()+signalRadius).parallel()
                .mapToObj(z -> {
                    int yLimits = signalRadius-Math.abs(z - getZ());
                    return IntStream.rangeClosed(getY()-yLimits, getY()+yLimits).parallel()
                            .mapToObj(y -> {
                                int xLimits = yLimits-Math.abs(y - getY());
                                return IntStream.rangeClosed(getX()-xLimits, getX()+xLimits).parallel()
                                        .mapToObj(x -> new Point3D(x,y,z)).collect(Collectors.toSet());
                            }).flatMap(Set::stream).collect(Collectors.toSet());
                }).flatMap(Set::stream).collect(Collectors.toSet());
    }

    public Set<Point3D> getLocationsOnManhattanDistanceLimitsAdjustedToScale(int scale, int ScaleStepSize){
        Set<Point3D> collect = IntStream.rangeClosed(getZ() - signalRadius, getZ() + signalRadius).parallel()
                .filter(z -> (z % (Math.max(4,scale/(ScaleStepSize^3))) == 0))
                .mapToObj(z -> {
                    int yLimits = signalRadius - Math.abs(z - getZ());
                    return IntStream.rangeClosed(getY() - yLimits, getY() + yLimits).parallel()
                            .filter(y -> (y % (Math.max(4,scale/(ScaleStepSize^3)))) == 0)
                            .mapToObj(y -> {
                                int xLimits = yLimits - Math.abs(y - getY());
                                return IntStream.of(getX() - xLimits, getX() + xLimits)
                                        .mapToObj(x -> new Point3D(x, y, z)).collect(Collectors.toSet());
                            }).flatMap(Set::stream).collect(Collectors.toSet());
                }).flatMap(Set::stream).collect(Collectors.toSet());
        return collect;
    }

    public Set<Point3D> getLocationsOnManhattanDistanceLimits(){
        Set<Point3D> collect = IntStream.rangeClosed(getZ() - signalRadius, getZ() + signalRadius)
                .mapToObj(z -> {
                    int yLimits = signalRadius - Math.abs(z - getZ());
                    return IntStream.rangeClosed(getY() - yLimits, getY() + yLimits)
                            .mapToObj(y -> {
                                int xLimits = yLimits - Math.abs(y - getY());
                                return IntStream.of(getX() - xLimits, getX() + xLimits)
                                        .mapToObj(x -> new Point3D(x, y, z)).collect(Collectors.toSet());
                            }).flatMap(Set::stream).collect(Collectors.toSet());
                }).flatMap(Set::stream).collect(Collectors.toSet());
        return collect;
    }


    public Set<Point3D> getLocationsWithinReachWithinLimits(int minX, int maxX, int minY, int maxY, int minZ, int maxZ){
        return IntStream.rangeClosed(Math.max(minZ, getZ()-signalRadius), Math.min(maxZ,getZ()+signalRadius))
                .mapToObj(z -> {
                    int yLimits = signalRadius-Math.abs(z - getZ());
                    return IntStream.rangeClosed(Math.max(minY, getY()-yLimits),Math.min(maxY,getY()+yLimits))
                            .mapToObj(y -> {
                                int xLimits = yLimits-Math.abs(y - getY());
                                return IntStream.rangeClosed(Math.max(minX,getX()-xLimits), Math.min(maxX,getX()+xLimits))
                                        .mapToObj(x -> new Point3D(x,y,z)).collect(Collectors.toSet());
                            }).flatMap(Set::stream).collect(Collectors.toSet());
                }).flatMap(Set::stream).collect(Collectors.toSet());
    }

    public Point3D getCoordinate() {
        return coordinate;
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
