package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

public class NanoBot {

    int signalRadius;
    Point3D coordinate;
    public NanoBot(String input){
        String[] split = input.split(">, r=");
        String substring = split[0].substring(5);
        String[] split1 = substring.split(",");
        coordinate = new Point3D(Integer.parseInt(split1[0]),Integer.parseInt(split1[2]),Integer.parseInt(split1[2]));
        signalRadius = Integer.parseInt(split[1]);
    }

    public int manhattandistanceTo(NanoBot other){
        return (int)(Math.abs(other.coordinate.getX() - this.coordinate.getX()) +
                Math.abs(other.coordinate.getY() - this.coordinate.getY()) +
        Math.abs(other.coordinate.getZ() - this.coordinate.getZ()));
    }

    public int getSignalRadius() {
        return signalRadius;
    }

}
