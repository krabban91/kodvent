package krabban91.kodvent.kodvent.y2018.d15;

import krabban91.kodvent.kodvent.utilities.Distances;
import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.search.Path;

import javax.print.attribute.standard.Destination;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class DistanceToPoint implements Path<Point> {
    Point current;
    int distance;
    Point target;
    Callable<Integer> heuristicFunction;

    DistanceToPoint previous;

    public DistanceToPoint(Point point, Point target) {
        this.current = point;
        this.distance = 0;
        this.target = target;
        this.heuristicFunction = () -> Distances.manhattan(current, target);
    }

    public DistanceToPoint(DistanceToPoint previous, Step edge) {
        this.previous = previous;
        this.current = edge.leadsTo(previous.destination());
        this.distance = edge.cost();
        this.target = previous.target;
        this.heuristicFunction = () -> Distances.manhattan(current, target);
    }

    public DistanceToPoint(DistanceToPoint other,  Callable<Integer> heuristicFunction) {
        this.previous = other.previous;
        this.current = other.current;
        this.distance = other.distance;
        this.target = other.target;
        this.heuristicFunction = heuristicFunction;
    }
    public DistanceToPoint(DistanceToPoint previous, Step edge, int heuristic) {
        this.previous = previous;
        this.current = edge.leadsTo(previous.destination());
        this.distance = edge.cost();
        this.target = previous.target;
        this.heuristic = heuristic;
    }

    public int heuristic(){
        int heuristic = 0;
        try {
            heuristic = heuristicFunction.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.cost() + heuristic;
    }

    public static int compare(DistanceToPoint l, DistanceToPoint r){
        if(l == null || r == null){
            return 0;
        }
        int compare = Integer.compare(l.cost(), r.cost());
        if(compare == 0){
            int compare1 = Integer.compare(l.current.y, r.current.y);
            if(compare1 == 0){
                return Integer.compare(l.current.x, r.current.x);
            }
            return compare1;
        }
        return compare;
    }

    public Set<Point> allVisited(){
        Set<Point> set = new HashSet<>();
        DistanceToPoint iterator = this;
        while (iterator != null){
            set.add(iterator.current);
            iterator = iterator.previous;
        }
        return set;
    }

    @Override
    public Point destination() {
        return current;
    }

    @Override
    public boolean hasVisited(Point destination) {
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
