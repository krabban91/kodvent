package krabban91.kodvent.kodvent.utilities;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Point3D {

    Point xy;
    int z;

    public Point3D(int x, int y, int z) {
        this.xy = new Point(x, y);
        this.z = z;
    }

    public static List<Point3D> getDirections() {
        Point3D north = new Point3D(0, -1, 0);
        Point3D south = new Point3D(0, 1, 0);
        Point3D east = new Point3D(1, 0, 0);
        Point3D west = new Point3D(-1, 0, 0);
        return Arrays.asList(north, south, east, west);
    }

    public boolean equals(Object other) {
        if (other instanceof Point3D) {
            Point3D point = (Point3D) other;
            boolean xyEq = xy.equals(point.xy);
            if (xyEq) {
                return this.z == point.z;
            }
            return xyEq;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xy, z);
    }

    public int manhattanDistance(Point3D other) {
        return Distances.manhattan(this, other);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Position<");
        builder.append(xy.x);
        builder.append(",");
        builder.append(xy.y);
        builder.append(",");
        builder.append(z);
        builder.append(">");
        return builder.toString();
    }

    public Point3D add(Point3D other) {
        return new Point3D(this.getX() + other.getX(),
                this.getY() + other.getY(),
                this.getZ() + other.getZ());
    }

    public Point3D copy() {
        return new Point3D(this.getX(), this.getY(), this.getZ());
    }

    public int getX() {
        return this.xy.x;
    }

    public int getY() {
        return this.xy.y;
    }

    public int getZ() {
        return this.z;
    }


}
