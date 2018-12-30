package krabban91.kodvent.kodvent.y2015.d09;

import krabban91.kodvent.kodvent.utilities.search.Edge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Road implements Edge {

    private Map<String, String> directions = new HashMap<>();
    private int distance;

    public Road(String input) {
        String[] split = input.split(" = ");
        distance = Integer.parseInt(split[1]);
        String[] split1 = split[0].split(" to ");
        directions.put(split1[0], split1[1]);
        directions.put(split1[1], split1[0]);
    }

    @Override
    public Set<String> vertices() {
        return directions.keySet();
    }

    @Override
    public String leadsTo(String from) {
        return directions.get(from);
    }

    @Override
    public int cost() {
        return getDistance();
    }

    @Override
    public boolean isConnectedTo(String city) {
        return directions.containsKey(city);
    }

    public int getDistance() {
        return distance;
    }
}
