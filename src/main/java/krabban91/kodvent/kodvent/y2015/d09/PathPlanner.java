package krabban91.kodvent.kodvent.y2015.d09;

import java.util.*;
import java.util.stream.Collectors;

public class PathPlanner {

    private final Set<String> cities;
    private final Map<String, List<Road>> network;

    public PathPlanner(List<Road> roads) {
        this.cities = roads.stream().map(Road::cities).flatMap(Set::stream).collect(Collectors.toSet());
        this.network = cities.stream()
                .collect(Collectors.toMap(
                        c -> c,
                        c -> roads.stream().filter(r -> r.isConnectedTo(c)).collect(Collectors.toList())));
    }

    public Route shortestRoute() {
        Deque<String> citiesToTry = new ArrayDeque<>(cities);
        Map<String, Route> salesmanPaths = new HashMap<>();
        while (!citiesToTry.isEmpty()) {
            String pop = citiesToTry.pop();
            PriorityQueue<Route> unchecked = new PriorityQueue<>(Comparator.comparingInt(Route::totalDistance));
            unchecked.add(new Route(pop));
            salesmanPaths.put(pop,bestSalesmanPath(unchecked));

        }
        return shortest(salesmanPaths);
    }

    public Route longestRoute() {
        Deque<String> citiesToTry = new ArrayDeque<>(cities);
        Map<String, Route> salesmanPaths = new HashMap<>();
        while (!citiesToTry.isEmpty()) {
            String pop = citiesToTry.pop();
            PriorityQueue<Route> unchecked = new PriorityQueue<>(Comparator.comparingInt(Route::totalDistance).reversed());
            unchecked.add(new Route(pop));
            salesmanPaths.put(pop,bestSalesmanPath(unchecked));
        }
        return longest(salesmanPaths);
    }

    public Route bestSalesmanPath( PriorityQueue<Route> unchecked) {
        while (!unchecked.isEmpty()) {
            Route poll = unchecked.poll();
            if (poll.reachedCities() == this.cities.size()) {
                return poll;
            }
            unchecked.addAll(network.get(poll.getCity())
                    .stream()
                    .filter(r -> !poll.containsCity(r.destination(poll.getCity())))
                    .map(r -> new Route(poll, r.destination(poll.getCity()), r.getDistance()))
                    .collect(Collectors.toList()));
        }
        return null;
    }

    public Route shortest(Map<String, Route> salesmanPaths) {
        return salesmanPaths.values().stream()
                .min(Comparator.comparingInt(Route::totalDistance))
                .orElse(null);
    }

    public Route longest(Map<String, Route> salesmanPaths) {
        return salesmanPaths.values().stream()
                .max(Comparator.comparingInt(Route::totalDistance))
                .orElse(null);
    }
}
