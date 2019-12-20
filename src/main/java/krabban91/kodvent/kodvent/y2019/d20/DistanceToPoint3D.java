package krabban91.kodvent.kodvent.y2019.d20;

import krabban91.kodvent.kodvent.utilities.Distances;
import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.search.Path;

import java.awt.*;

public class DistanceToPoint3D implements Path<Point3D> {
    Point3D current;
    int distance;
    Point3D target;
    int heuristic;

    DistanceToPoint3D previous;

    public DistanceToPoint3D(Point3D point, Point3D target) {
        this.current = point;
        this.distance = 0;
        this.target = target;
        this.heuristic = Distances.manhattan(current, target);
    }

    public DistanceToPoint3D(DistanceToPoint3D previous, Step3D edge) {
        this.previous = previous;
        this.current = edge.leadsTo(previous.destination());
        this.distance = edge.cost();
        this.target = previous.target;
        this.heuristic = Distances.manhattan(current, target);
    }
    public DistanceToPoint3D(DistanceToPoint3D previous, Step3D edge, int heuristic) {
        this.previous = previous;
        this.current = edge.leadsTo(previous.destination());
        this.distance = edge.cost();
        this.target = previous.target;
        this.heuristic = heuristic;
    }

    public int heuristic(){
        return this.cost() + heuristic;
    }

    public static int compare(DistanceToPoint3D l, DistanceToPoint3D r){
        if(l == null || r == null){
            return 0;
        }
        int compare = Integer.compare(l.cost(), r.cost());
        if(compare == 0){
            int compare1 = Integer.compare(l.current.getY(), r.current.getY());
            if(compare1 == 0){
                return Integer.compare(l.current.getX(), r.current.getX());
            }
            return compare1;
        }
        return compare;
    }

    @Override
    public Point3D destination() {
        return current;
    }

    @Override
    public boolean hasVisited(Point3D destination) {
        return this.destination().equals(destination) || (this.previous != null && this.previous.hasVisited(destination));
    }

    @Override
    public int cost() {
        return this.distance + (this.previous != null ? this.previous.cost() : 0);
    }

    @Override
    public boolean isTarget() {
        return target.equals(current);
    }
}
