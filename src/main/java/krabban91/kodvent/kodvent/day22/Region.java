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

    public boolean canBeReachedWithTool(Tool tool){
        switch (this.type){
            case ROCKY: return tool.equals(Tool.CLIMBING) || tool.equals(Tool.TORCH);
            case WET: return tool.equals(Tool.CLIMBING) || tool.equals(Tool.NEITHER);
            default: return tool.equals(Tool.TORCH) || tool.equals(Tool.NEITHER);
        }
    }
}
