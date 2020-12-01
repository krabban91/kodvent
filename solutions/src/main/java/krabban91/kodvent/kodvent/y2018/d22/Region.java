package krabban91.kodvent.kodvent.y2018.d22;

import java.awt.*;

public class Region {
    RegionType type;
    Point point;

    public Region(Point point, RegionType type) {
        this.point = point;
        this.type = type;
    }

    public RegionType getType() {
        return type;
    }

    public Point getPoint() {
        return point;
    }

    public boolean canBeReachedWithTool(Tool tool) {
        switch (this.type) {
            case ROCKY:
                return tool.equals(Tool.CLIMBING) || tool.equals(Tool.TORCH);
            case WET:
                return tool.equals(Tool.CLIMBING) || tool.equals(Tool.NEITHER);
            default:
                return tool.equals(Tool.TORCH) || tool.equals(Tool.NEITHER);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Region) {
            Region other = (Region) obj;
            return this.getPoint().equals(other.getPoint());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 2 * this.getPoint().hashCode();
    }
}
