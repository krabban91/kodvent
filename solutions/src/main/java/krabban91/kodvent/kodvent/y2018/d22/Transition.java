package krabban91.kodvent.kodvent.y2018.d22;

import krabban91.kodvent.kodvent.utilities.search.Edge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Transition implements Edge<Mode> {

    private final int cost;
    private final Map<Mode, Mode> directions = new HashMap<>();

    public Transition(Mode from, Mode to, int cost) {
        directions.put(from, to);
        this.cost = cost;
    }

    @Override
    public Set<Mode> vertices() {
        return directions.keySet();
    }

    @Override
    public Mode leadsTo(Mode from) {
        return directions.get(from);
    }

    @Override
    public boolean isConnectedTo(Mode vertex) {
        return directions.containsKey(vertex) || directions.values().stream().anyMatch(r -> r.equals(vertex));
    }

    @Override
    public int cost() {
        return cost;
    }
}
