package krabban91.kodvent.kodvent.y2019.d18;

import krabban91.kodvent.kodvent.utilities.Distances;
import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.y2018.d15.DistanceToPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TSPState {
    private final List<Integer> takenKeys;
    private final LinkedList<DistanceToPoint> path;
    private final Map<Integer, Set<Integer>> dependencies;
    private final Map<Integer, Set<Integer>> passedKeys;
    private final Map<Point, Integer> keys;
    private final Map<Point, Integer> doors;

    public TSPState(List<Integer> takenKeys, List<DistanceToPoint> path, Map<Integer, Set<Integer>> dependencies, Map<Integer, Set<Integer>> passedKeys, Map<Point, Integer> keys, Map<Point, Integer> doors) {
        this.takenKeys = new ArrayList<>(takenKeys);
        this.path = new LinkedList<>(path);
        this.dependencies = deepCopyDependencies(dependencies);
        this.passedKeys = deepCopyDependencies(passedKeys);
        this.keys = new HashMap<>(keys);
        this.doors = new HashMap<>(doors);
    }


    public TSPState copy() {
        return new TSPState(this.takenKeys, this.path, this.dependencies, this.passedKeys, this.keys, this.doors);
    }

    public int cost() {
        return this.path.stream().mapToInt(DistanceToPoint::cost).sum();
    }

    public void walkTo(DistanceToPoint next, Map<Integer, Point> keyLookup) {
        Integer removedKey = this.keys.remove(next.destination());
        DistanceToPoint searchCopy = new DistanceToPoint(next,
                () -> keys.values().stream().map(keyLookup::get).mapToInt(p -> Distances.manhattan(p, next.destination())).sum());
        dependencies.remove(removedKey);
        passedKeys.remove(removedKey);
        dependencies.forEach((i, s) -> s.remove(removedKey));
        passedKeys.forEach((i, s) -> s.remove(removedKey));
        doors.entrySet().stream()
                .filter(i -> removedKey == i.getValue() + Day18.UPPER_TO_LOWER)
                .findFirst().ifPresent(entry -> doors.remove(entry.getKey()));
        this.takenKeys.add(removedKey);
        this.path.addLast(searchCopy);
    }

    public List<Point> targets(Map<Integer, Point> keyLookup, Set<List<Integer>> visited){
        // TODO : This generated a heap space overflow for more possible paths than 8 and using BFS
        return dependencies.entrySet().stream()
                .filter(e -> e.getValue().isEmpty())
                .filter(e -> !takenKeys.contains(e.getKey()))
                .filter(e -> passedKeys.get(e.getKey()).isEmpty())
                .map(e -> keyLookup.get(e.getKey()))
                .collect(Collectors.toList());

    }

    public Point currentLocation(){
        return this.path.getLast().destination();
    }

    public List<Integer> getKey() {
        return this.takenKeys;
    }

    public Map<Point, Integer> getKeys() {
        return keys;
    }


    private static Map<Integer, Set<Integer>> deepCopyDependencies(Map<Integer, Set<Integer>> dependencies) {
        return dependencies.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new HashSet<>(e.getValue())));
    }

}
