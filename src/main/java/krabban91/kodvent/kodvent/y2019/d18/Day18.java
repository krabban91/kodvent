package krabban91.kodvent.kodvent.y2019.d18;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.utilities.search.Graph;
import krabban91.kodvent.kodvent.y2018.d15.DistanceToPoint;
import krabban91.kodvent.kodvent.y2018.d15.Step;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day18 {
    public static final int DOT = '.';
    private static final int WALL = 35;
    private static final int A = 65;
    private static final int a = 97;
    private static final int UPPER_TO_LOWER = a - A;
    private static final int Z = 90;
    private static final int z = 122;
    private static final int AT = 64;
    private static final Point VEC_NORTH = new Point(0, -1);
    private static final Point VEC_EAST = new Point(1, 0);
    private static final Point VEC_SOUTH = new Point(0, 1);
    private static final Point VEC_WEST = new Point(-1, 0);
    private static final List<Point> VECTORS = Arrays.asList(VEC_NORTH, VEC_SOUTH, VEC_WEST, VEC_EAST);
    List<List<Integer>> in;
    boolean debug = false;

    public Day18() {
        System.out.println("::: Starting Day 18 :::");
        String inputPath = "y2019/d18/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        final Map<Point, Integer> map = IntStream.range(0, in.size())
                .mapToObj(y -> IntStream.range(0, in.get(y).size())
                        .boxed()
                        .collect(Collectors.toMap(e -> new Point(e, y), e -> in.get(y).get(e)))
                        .entrySet())
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Map<Point, Integer> keys = map.entrySet().stream()
                .filter(e -> e.getValue() >= a && e.getValue() <= z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Map<Integer, Point> keyLookup = keys.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        final Map<Point, Integer> doors = map.entrySet().stream()
                .filter(e -> e.getValue() >= A && e.getValue() <= Z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Point startLocation = map.entrySet().stream()
                .filter(e -> e.getValue() == AT)
                .findFirst()
                .map(Map.Entry::getKey)
                .get();


        Map<Point, Collection<Step>> network = getNetwork(map, true); // ignored
        Map<Map.Entry<Point, Integer>, DistanceToPoint> pathsToKeys = keys.entrySet().stream().collect(Collectors.toMap(e -> e, e -> {
            Map<Point, DistanceToPoint> checked = new HashMap<>();
            PriorityQueue<DistanceToPoint> unChecked = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint::heuristic));
            unChecked.add(new DistanceToPoint(startLocation, e.getKey()));
            Graph<DistanceToPoint, Step, Point> graph = new Graph<>();
            return graph.search(unChecked, (p, net) -> this.addNext(p, net, checked, unChecked), network);
        }));

        Map<Integer, List<Integer>> dependencyKeys = pathsToKeys.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getValue(), e -> e.getValue().allVisited().stream()
                        .filter(doors::containsKey)
                        .map(doors::get)
                        .map(i -> i + UPPER_TO_LOWER)
                        .collect(Collectors.toList())));
        Map<Integer, Set<Integer>> dependencyGraph = dependencyKeys.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    Set<Integer> neededKeys = new HashSet<>();
                    List<Integer> other = e.getValue();
                    while (!other.isEmpty()) {
                        neededKeys.addAll(other);
                        other = other.stream().map(dependencyKeys::get).flatMap(Collection::stream).collect(Collectors.toList());
                    }
                    return neededKeys;
                }));

        // For each key:
        // - A* from start
        //   gather the dependency graph (f-> d+e, d ->[], e-> [])
        int stepsTaken = 0;
        logMap(map);
        Point current = startLocation;
        while (!keys.isEmpty()) {
            // goal: reach dependency with most deps.
            // example above -> f needs d and e. get all with no dependencies
            // take the one with shortest total path. (
            // gives order d->e->f or e->d->f. which has least steps?
            Point target = dependencyGraph.entrySet().stream()
                    .filter(e -> e.getValue().isEmpty())
                    // Choosing the key needed to reach the most doors.
                    // TODO: Should test multiple paths
                    .max(Comparator.comparingLong(e -> dependencyGraph.values().stream().filter(s -> s.contains(e.getKey())).count()))
                    .map(e -> keyLookup.get(e.getKey()))
                    .get();
            Map<Point, Collection<Step>> networkWithDoors = getNetwork(map, false); // ignored
            Map<Point, DistanceToPoint> checked = new HashMap<>();
            PriorityQueue<DistanceToPoint> unChecked = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint::heuristic));
            unChecked.add(new DistanceToPoint(current, target));
            Graph<DistanceToPoint, Step, Point> graph = new Graph<>();
            DistanceToPoint search = graph.search(unChecked, (p, net) -> this.addNext(p, net, checked, unChecked), networkWithDoors, p -> keys.containsKey(p.destination()));

            // move
            // move to key location
            map.put(current, DOT);
            stepsTaken += search.cost();
            current = search.destination();
            map.put(current, AT);
            // remove key from map
            // pick up all keys that I pass by.
            List<Point> pickedUpKeys = search.allVisited().stream().filter(keys::containsKey)
                    .collect(Collectors.toList());
            List<Integer> keysInPath = pickedUpKeys.stream().map(keys::remove).collect(Collectors.toList());
            keysInPath.forEach(removedKey -> {
                dependencyGraph.remove(removedKey);
                dependencyGraph.forEach((i, s) -> s.remove(removedKey));
                // unlock door connected to key
                doors.entrySet().stream()
                        .filter(i -> removedKey == i.getValue() + UPPER_TO_LOWER)
                        .findFirst().ifPresent(entry -> {
                    doors.remove(entry.getKey());
                    map.put(entry.getKey(), DOT);
                });

            });
            logMap(map);
        }

        // return number of steps taken
        // 6534 is too high.
        return stepsTaken;
    }

    private void logMap(Map<Point, Integer> map) {
        if (debug) {
            System.out.println(new LogUtils<Integer>().mapToText(map, v -> v == null ? " " : (char) v.intValue() + ""));
        }
    }

    private Collection<DistanceToPoint> addNext(DistanceToPoint p, Map<Point, Collection<Step>> net, Map<Point, DistanceToPoint> checked, PriorityQueue<DistanceToPoint> unChecked) {
        checked.putIfAbsent(p.destination(), p);
        return net.get(p.destination()).stream()
                .map(w -> new DistanceToPoint(p, w))
                .filter(d -> !p.hasVisited(d.destination()))
                .filter(s -> !checked.containsKey(s.destination()))
                .filter(d -> unChecked.stream().noneMatch(other -> other.destination().equals(d.destination())))
                .collect(Collectors.toSet());
    }

    private Map<Point, Collection<Step>> getNetwork(Map<Point, Integer> map, boolean ignoreDoors) {
        return map.entrySet().stream()
                .filter(e -> e.getValue() != WALL)
                .map(Map.Entry::getKey)
                .collect(Collectors.toMap(e -> e, (e -> VECTORS.stream()
                        .map(p -> new Point(p.x + e.x, p.y + e.y))
                        .filter(map::containsKey)
                        .filter(p -> map.get(p) != WALL)
                        .filter(p -> !(map.get(p) >= A && map.get(p) <= Z) || ignoreDoors)
                        .map(p -> new Step(e, p))
                        .collect(Collectors.toList()))));
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {

        in = Input.getLines(inputPath).stream().map(s -> s.chars().boxed().collect(Collectors.toList())).collect(Collectors.toList());
    }
}
