package krabban91.kodvent.kodvent.y2019.d20;

import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.search.Edge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Edge3D implements Edge<Point3D> {

    private Map<Point3D, Point3D> directions = new HashMap<>();

    public Edge3D(Point3D from, Point3D to){
        directions.put(from, to);
        directions.put(to, from);
    }

    @Override
    public Set<Point3D> vertices() {
        return directions.keySet();
    }

    @Override
    public Point3D leadsTo(Point3D from) {
        return directions.get(from);
    }

    @Override
    public boolean isConnectedTo(Point3D vertex) {
        return directions.containsKey(vertex);
    }

    @Override
    public int cost() {
        return 1;
    }
}
