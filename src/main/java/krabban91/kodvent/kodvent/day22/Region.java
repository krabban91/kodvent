package krabban91.kodvent.kodvent.day22;

import java.awt.*;

public class Region {
    RegionType type;
    Point point;

    public Region(Point point, RegionType type){
        this.point = point;
        this.type = type;
    }

    public RegionType getType() {
        return type;
    }

    public Point getPoint() {
        return point;
    }
}
