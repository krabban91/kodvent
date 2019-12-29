package krabban91.kodvent.kodvent.y2019.d18;

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
    private final Map<Integer, Set<Integer>> neededKeysBefore;
    private final Map<Integer, Set<Integer>> keysInTheWay;
    private final Map<Point, Integer> keys;

    public TSPState(List<Integer> takenKeys, List<DistanceToPoint> path, Map<Integer, Set<Integer>> neededKeysBefore, Map<Integer, Set<Integer>> keysInTheWay, Map<Point, Integer> keys) {
        this.takenKeys = new ArrayList<>(takenKeys);
        this.path = new LinkedList<>(path);
        this.neededKeysBefore = deepCopyDependencies(neededKeysBefore);
        this.keysInTheWay = deepCopyDependencies(keysInTheWay);
        this.keys = new HashMap<>(keys);
    }


    public TSPState copy() {
        return new TSPState(this.takenKeys, this.path, this.neededKeysBefore, this.keysInTheWay, this.keys);
    }

    public int cost() {
        return this.path.stream().mapToInt(DistanceToPoint::cost).sum();
    }

    public int walkTo(DistanceToPoint next) {
        Integer removedKey = this.keys.remove(next.destination());
        unlockKey(removedKey);
        this.takenKeys.add(removedKey);
        this.path.addLast(next);
        return removedKey;
    }

    public void unlockKey(int key){
        neededKeysBefore.remove(key);
        keysInTheWay.remove(key);
        neededKeysBefore.forEach((i, s) -> s.remove(key));
        keysInTheWay.forEach((i, s) -> s.remove(key));

    }

    public List<Point> targets(Map<Integer, Point> keyLookup){
        // TODO : This generated a heap space overflow for more possible paths than 8 and using BFS
        return neededKeysBefore.entrySet().stream()
                .filter(e -> e.getValue().isEmpty())
                .filter(e -> !takenKeys.contains(e.getKey()))
                .filter(e -> keysInTheWay.get(e.getKey()).isEmpty())
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

    public static boolean isSameState(TSPState one, TSPState another){
        boolean sizeMatch = one.takenKeys.size() == another.takenKeys.size();
        if(sizeMatch){
            if(one.takenKeys.size() == 0){
                return true;
            }
            boolean lastMatch = one.takenKeys.get(one.takenKeys.size() - 1).equals(another.takenKeys.get(another.takenKeys.size() - 1));
            if(lastMatch){
                boolean sameSet = new HashSet<>(one.takenKeys).equals(new HashSet<>(another.takenKeys));
                return sameSet;
            }
        }
        return false;
    }

}
