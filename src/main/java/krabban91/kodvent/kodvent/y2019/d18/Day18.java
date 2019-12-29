package krabban91.kodvent.kodvent.y2019.d18;

import krabban91.kodvent.kodvent.utilities.Distances;
import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.utilities.search.Graph;
import krabban91.kodvent.kodvent.y2018.d15.DistanceToPoint;
import krabban91.kodvent.kodvent.y2018.d15.Step;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
    public static final int UPPER_TO_LOWER = a - A;
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
        long part1 = -1;//getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = -1;//getPart2();
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
        int maxX = map.keySet().stream().mapToInt(p -> p.x).max().orElse(0);
        int maxY = map.keySet().stream().mapToInt(p -> p.y).max().orElse(0);

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
        Map<Map.Entry<Point, Integer>, DistanceToPoint> pathsToKeys = keys.entrySet().stream().collect(Collectors.toMap(e -> e, e -> getDistanceToPoint(startLocation, e.getKey(), network)));


        Map<Integer, Set<Integer>> passedKeys = pathsToKeys.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getValue(), e -> e.getValue().allVisited().stream()
                        .filter(keys::containsKey)
                        .map(keys::get)
                        .filter(i -> !i.equals(e.getKey().getValue()))
                        .collect(Collectors.toSet())));
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
        Map<Point, Map<Point, DistanceToPoint>> distancesFromTo = getDistancesFromTo(startLocation, map, keys);

        int stepsTaken = Integer.MAX_VALUE;
        logMap(map);
        Set<List<Integer>> visitedStates = new HashSet<>();
        // TODO: Setup a heuristic that is useful
        PriorityQueue<TSPState> priorityQueueStates = new PriorityQueue<>(Comparator.comparingInt(TSPState::cost));
        priorityQueueStates.add(new TSPState(Collections.emptyList(), Collections.singletonList(new DistanceToPoint(startLocation, startLocation)), dependencyGraph, passedKeys, keys, doors));
        while (!priorityQueueStates.isEmpty()) {
            TSPState poll = priorityQueueStates.poll().copy();
            int stepsTakenSoFar = poll.cost();
            if (poll.getKeys().isEmpty()) {
                if (stepsTaken > stepsTakenSoFar) {
                    stepsTaken = stepsTakenSoFar;
                    break;
                }
            }
            List<Point> targets = poll.targets(keyLookup, visitedStates);
            for (Point target : targets) {
                TSPState copy = poll.copy();
                DistanceToPoint search = distancesFromTo.get(copy.currentLocation()).get(target);
                copy.walkTo(search, keyLookup);
                if(visitedStates.add(copy.getKey())){
                    int stepsTakenSoFarIsh = copy.cost();
                    if (stepsTakenSoFarIsh < stepsTaken) {
                        priorityQueueStates.add(copy);
                    }
                }

            }
        }
        // return number of steps taken
        // 6534 is too high. 5680 is wrong, 5636 is wrong, 5594 is wrong
        return stepsTaken;
    }

    private DistanceToPoint getDistanceToPoint(Point from, Point to, Map<Point, Collection<Step>> network) {
        Map<Point, DistanceToPoint> checked = new HashMap<>();
        PriorityQueue<DistanceToPoint> unChecked = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint::heuristic));
        unChecked.add(new DistanceToPoint(from, to));
        Graph<DistanceToPoint, Step, Point> graph = new Graph<>();
        return graph.search(unChecked, (p, net) -> this.addNext(p, net, checked, unChecked), network);
    }

    private Map<Point, Map<Point, DistanceToPoint>> getDistancesFromTo(Point start, Map<Point, Integer> map, Map<Point, Integer> keys) {
        Map<Point, Collection<Step>> network = getNetwork(map, true);
        Map<Point, Map<Point, DistanceToPoint>> distances = new HashMap<>();
        distances.put(start, getDistancesToKeys(start, keys, network));
        for (Point from : keys.keySet()) {
            distances.put(from, getDistancesToKeys(from, keys, network));

        }
        return distances;
    }

    private Map<Point, DistanceToPoint> getDistancesToKeys(Point start, Map<Point, Integer> keys, Map<Point, Collection<Step>> network) {
        return keys.keySet().stream()
                .map(integer -> getDistanceToPoint(start, integer, network))
                .map(d -> new Step(start, d.destination(), d.cost()))
                .map(s -> new DistanceToPoint(new DistanceToPoint(start, s.leadsTo(start)), s))
                .collect(Collectors.toMap(DistanceToPoint::destination, e -> e));
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
