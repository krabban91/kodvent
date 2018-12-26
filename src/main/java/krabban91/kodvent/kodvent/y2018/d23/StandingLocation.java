package krabban91.kodvent.kodvent.y2018.d23;

import krabban91.kodvent.kodvent.y2018.d11.Point3D;

public class StandingLocation {

    Point3D point;
    Point3D target;

    int distance;

    public StandingLocation(Point3D point) {
        this.point = point;
        this.target = point;
        this.distance = manhattandistanceTo(target);
    }

    public int manhattandistanceTo(Point3D other) {
        return (int) (Math.abs(other.getX() - this.point.getX()) +
                Math.abs(other.getY() - this.point.getY()) +
                Math.abs(other.getZ() - this.point.getZ()));
    }

    public Point3D getPoint() {
        return point;
    }

    public void setPoint(Point3D point) {
        this.point = point;
        this.distance = manhattandistanceTo(target);
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StandingLocation) {
            StandingLocation other = (StandingLocation) obj;
            return this.getPoint().equals(other.getPoint());
        }
        return false;
    }
}

