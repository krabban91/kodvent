package krabban91.kodvent.kodvent.y2015.d09;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Road {

    private Map<String, String> directions = new HashMap<>();
    int distance;

    public Road(String input) {
        String[] split = input.split(" = ");
        distance = Integer.parseInt(split[1]);
        String[] split1 = split[0].split(" to ");
        directions.put(split1[0], split1[1]);
        directions.put(split1[1], split1[0]);
    }

    public Set<String> cities() {
        return directions.keySet();
    }

    public String destination(String from) {
        return directions.get(from);
    }

    public boolean isConnectedTo(String city) {
        return directions.containsKey(city);
    }

    public int getDistance() {
        return distance;
    }
}
