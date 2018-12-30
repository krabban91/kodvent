package krabban91.kodvent.kodvent.y2015.d09;

import krabban91.kodvent.kodvent.utilities.search.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class PathPlanner {

    private final Set<String> cities;
    private final Map<String, Collection<Road>> network;

    public PathPlanner(List<Road> roads) {
        this.cities = roads.stream().map(Road::vertices).flatMap(Set::stream).collect(Collectors.toSet());
        this.network = cities.stream()
                .collect(Collectors.toMap(
                        c -> c,
                        c -> roads.stream().filter(r -> r.isConnectedTo(c)).collect(Collectors.toList())));
    }

    public Route shortestRoute() {
        Deque<String> citiesToTry = new ArrayDeque<>(cities);
        Map<String, Route> salesmanPaths = new HashMap<>();
        Graph<Route, Road> graph = new Graph<>();
        while (!citiesToTry.isEmpty()) {
            String pop = citiesToTry.pop();
            PriorityQueue<Route> unchecked = new PriorityQueue<>(Comparator.comparingInt(Route::cost));
            unchecked.add(new Route(pop, this.cities.size()));
            salesmanPaths.put(pop, graph.search(unchecked, this::nextPlaces, this.network));
        }
        return shortest(salesmanPaths);
    }

    public Route longestRoute() {
        Deque<String> citiesToTry = new ArrayDeque<>(cities);
        Map<String, Route> salesmanPaths = new HashMap<>();
        Graph<Route, Road> graph = new Graph<>();
        while (!citiesToTry.isEmpty()) {
            String pop = citiesToTry.pop();
            PriorityQueue<Route> unchecked = new PriorityQueue<>(Comparator.comparingInt(Route::cost).reversed());
            unchecked.add(new Route(pop, this.cities.size()));
            salesmanPaths.put(pop, graph.search(unchecked, this::nextPlaces, this.network));
        }
        return longest(salesmanPaths);
    }

    public Set<Route> nextPlaces(Route route, Map<String, Collection<Road>> network) {
        return network.get(route.destination())
                .stream()
                .filter(r -> !route.hasVisited(r.leadsTo(route.destination())))
                .map(r -> new Route(route, r, route.getTargetSize()))
                .collect(Collectors.toSet());
    }

    public Route shortest(Map<String, Route> salesmanPaths) {
        return salesmanPaths.values().stream()
                .min(Comparator.comparingInt(Route::cost))
                .orElse(null);
    }

    public Route longest(Map<String, Route> salesmanPaths) {
        return salesmanPaths.values().stream()
                .max(Comparator.comparingInt(Route::cost))
                .orElse(null);
    }
}
