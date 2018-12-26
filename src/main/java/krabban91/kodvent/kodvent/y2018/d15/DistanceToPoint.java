package krabban91.kodvent.kodvent.y2018.d15;

import krabban91.kodvent.kodvent.utilities.Distances;

import java.awt.*;

public class DistanceToPoint {
    Point point;
    int distance;
    Point target;
    int heuristic;
    public DistanceToPoint(Point point, int distance,Point target){
        this.point = point;
        this.distance = distance;
        this.target = target;
        this.heuristic = Distances.manhattan(point,target);
    }

    public Point getPoint() {
        return point;
    }

    public int getDistance() {
        return distance;
    }

    public int heuristic(){
        return distance + heuristic;
    }

    public static int compare(DistanceToPoint l, DistanceToPoint r){
        if(l == null || r == null){
            return 0;
        }
        int compare = Integer.compare(l.distance, r.distance);
        if(compare == 0){
            int compare1 = Integer.compare(l.point.y, r.point.y);
            if(compare1 == 0){
                return Integer.compare(l.point.x,r.point.x);
            }
            return compare1;
        }
        return compare;
    }
}
