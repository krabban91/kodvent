package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

import java.util.List;
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
        return (int)(Math.abs(other.getX() - this.coordinate.getX()) +
                Math.abs(other.getY() - this.coordinate.getY()) +
                Math.abs(other.getZ() - this.coordinate.getZ()));
    }

    public int getSignalRadius() {
        return signalRadius;
    }

    public Set<Point3D> getLocationsWithinReach(){
        return IntStream.rangeClosed(getZ()-signalRadius, getZ()+signalRadius)
                .mapToObj(z -> {
                    int yLimits = signalRadius-Math.abs(z - getZ());
                    return IntStream.rangeClosed(getY()-yLimits, getY()+yLimits)
                            .mapToObj(y -> {
                                int xLimits = yLimits-Math.abs(y - getY());
                                return IntStream.rangeClosed(getX()-xLimits, getX()+xLimits)
                                        .mapToObj(x -> new Point3D(x,y,z)).collect(Collectors.toSet());
                            }).flatMap(Set::stream).collect(Collectors.toSet());
                }).flatMap(Set::stream).collect(Collectors.toSet());
    }

    private int getX(){
        return (int)coordinate.getX();
    }

    private int getY(){
        return (int)coordinate.getY();
    }
    private int getZ(){
        return (int)coordinate.getZ();
    }
}
