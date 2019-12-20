package krabban91.kodvent.kodvent.y2019.d20;

import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.search.Path;

import java.util.concurrent.Callable;

public class DistanceToPoint3D implements Path<Point3D> {
    Point3D current;
    int distance;
    Point3D target;
    DistanceToPoint3D previous;
    private Callable<Integer> heuristicFunction;

    public DistanceToPoint3D(Point3D point, Point3D target) {
        this.current = point;
        this.distance = 0;
        this.target = target;
        heuristicFunction = () -> 0;
    }

    public DistanceToPoint3D(DistanceToPoint3D previous, Step3D edge, Callable<Integer> heuristicFunction) {
        this.previous = previous;
        this.current = edge.leadsTo(previous.destination());
        this.distance = edge.cost();
        this.target = previous.target;
        this.heuristicFunction = heuristicFunction;
    }

    public int heuristic() {
        int heuristic = 0;
        try {
            heuristic = this.heuristicFunction.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.cost() + heuristic;
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
