package krabban91.kodvent.kodvent.y2018.d15;

import krabban91.kodvent.kodvent.utilities.search.Edge;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Step implements Edge<Point> {

    private Map<Point, Point> directions = new HashMap<>();

    public Step(Point from, Point to){
        directions.put(from, to);
        directions.put(to, from);
    }

    @Override
    public Set<Point> vertices() {
        return directions.keySet();
    }

    @Override
    public Point leadsTo(Point from) {
        return directions.get(from);
    }

    @Override
    public boolean isConnectedTo(Point vertex) {
        return directions.containsKey(vertex);
    }

    @Override
    public int cost() {
        return 1;
    }
}
