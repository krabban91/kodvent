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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        final Map<Point, Integer> keys = map.entrySet().stream()
                .filter(e -> e.getValue() >= a && e.getValue() <= z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Map<Integer, Point> keyLookup = keys.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        final Point startLocation = map.entrySet().stream()
                .filter(e -> e.getValue() == AT)
                .findFirst()
                .map(Map.Entry::getKey)
                .get();

        TSPState initialState = getStartingState(map, keys, startLocation);

        Map<Point, Map<Point, DistanceToPoint>> distancesFromTo = getDistancesFromTo(startLocation, map, keys);
        logMap(map);
        Map<TSPState, Integer> visitedStates = new HashMap<>();
        PriorityQueue<TSPState> priorityQueueStates = new PriorityQueue<>(Comparator.comparingInt(TSPState::cost));
        priorityQueueStates.add(initialState);
        while (!priorityQueueStates.isEmpty()) {
            TSPState poll = priorityQueueStates.poll();
            if (poll.getKeys().isEmpty()) {
                return poll.cost();
            }
            List<Point> targets = poll.targets(keyLookup);
            for (Point target : targets) {
                TSPState next = poll.copy();
                DistanceToPoint search = distancesFromTo.get(next.currentLocation()).get(target);
                next.walkTo(search);

                Optional<Map.Entry<TSPState, Integer>> any = visitedStates.entrySet().stream()
                        .filter(l -> TSPState.isSameState(l.getKey(), next))
                        .findAny();
                int cost = next.cost();
                if (any.isPresent()) {
                    if (any.get().getValue() > cost) {
                        TSPState previousKey = any.get().getKey();
                        priorityQueueStates.removeIf(s -> s.equals(previousKey));
                        visitedStates.remove(previousKey);
                        visitedStates.put(next, cost);
                        priorityQueueStates.add(next);
                    }
                } else {
                    visitedStates.put(next, cost);
                    priorityQueueStates.add(next);
                }
            }
        }
        return -1;
    }

    private TSPState getStartingState(Map<Point, Integer> map, Map<Point, Integer> keys, Point startLocation) {
        Map<Point, Collection<Step>> network = getNetwork(map); // ignored
        Map<Map.Entry<Point, Integer>, DistanceToPoint> pathsToKeys = keys.entrySet().stream().collect(Collectors.toMap(e -> e, e -> getDistanceToPoint(startLocation, e.getKey(), network)));
        final Map<Point, Integer> doors = map.entrySet().stream()
                .filter(e -> e.getValue() >= A && e.getValue() <= Z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<Integer, Set<Integer>> keysInTheWay = pathsToKeys.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getValue(), e -> e.getValue().allVisited().stream()
                        .filter(keys::containsKey)
                        .map(keys::get)
                        .filter(i -> !i.equals(e.getKey().getValue()))
                        .collect(Collectors.toSet())));
        Map<Integer, Set<Integer>> neededKeys = pathsToKeys.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getValue(), e -> e.getValue().allVisited().stream()
                        .filter(doors::containsKey)
                        .map(doors::get)
                        .map(i -> i + UPPER_TO_LOWER)
                        .collect(Collectors.toSet())));
        return new TSPState(Collections.emptyList(), Collections.singletonList(new DistanceToPoint(startLocation, startLocation)), neededKeys, keysInTheWay, keys);
    }

    private DistanceToPoint getDistanceToPoint(Point from, Point to, Map<Point, Collection<Step>> network) {
        Map<Point, DistanceToPoint> checked = new HashMap<>();
        PriorityQueue<DistanceToPoint> unChecked = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint::heuristic));
        unChecked.add(new DistanceToPoint(from, to));
        Graph<DistanceToPoint, Step, Point> graph = new Graph<>();
        return graph.search(unChecked, (p, net) -> this.addNext(p, net, checked, unChecked), network);
    }

    private Map<Point, Map<Point, DistanceToPoint>> getDistancesFromTo(Point start, Map<Point, Integer> map, Map<Point, Integer> keys) {
        Map<Point, Collection<Step>> network = getNetwork(map);
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

    private Map<Point, Collection<Step>> getNetwork(Map<Point, Integer> map) {
        return map.entrySet().stream()
                .filter(e -> e.getValue() != WALL)
                .map(Map.Entry::getKey)
                .collect(Collectors.toMap(e -> e, (e -> VECTORS.stream()
                        .map(p -> new Point(p.x + e.x, p.y + e.y))
                        .filter(map::containsKey)
                        .filter(p -> map.get(p) != WALL)
                        .map(p -> new Step(e, p))
                        .collect(Collectors.toList()))));
    }

    public long getPart2() {
        final Map<Point, Integer> map = IntStream.range(0, in.size())
                .mapToObj(y -> IntStream.range(0, in.get(y).size())
                        .boxed()
                        .collect(Collectors.toMap(e -> new Point(e, y), e -> in.get(y).get(e)))
                        .entrySet())
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Point startLocation = map.entrySet().stream()
                .filter(e -> e.getValue() == AT)
                .findFirst()
                .map(Map.Entry::getKey)
                .get();
        Point start1 = new Point(startLocation.x - 1, startLocation.y - 1);
        Point start2 = new Point(startLocation.x + 1, startLocation.y - 1);
        Point start3 = new Point(startLocation.x - 1, startLocation.y + 1);
        Point start4 = new Point(startLocation.x + 1, startLocation.y + 1);
        map.put(start1, AT);
        map.put(new Point(startLocation.x, startLocation.y - 1), WALL);
        map.put(start2, AT);
        map.put(new Point(startLocation.x - 1, startLocation.y), WALL);
        map.put(new Point(startLocation.x, startLocation.y), WALL);
        map.put(new Point(startLocation.x + 1, startLocation.y), WALL);
        map.put(start3, AT);
        map.put(new Point(startLocation.x, startLocation.y + 1), WALL);
        map.put(start4, AT);

        Map<Point, Integer> map1 = map.entrySet().stream().filter(e -> e.getKey().y <= startLocation.y && e.getKey().x <= startLocation.x)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<Point, Integer> map2 = map.entrySet().stream().filter(e -> e.getKey().y <= startLocation.y && e.getKey().x >= startLocation.x)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<Point, Integer> map3 = map.entrySet().stream().filter(e -> e.getKey().y >= startLocation.y && e.getKey().x <= startLocation.x)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<Point, Integer> map4 = map.entrySet().stream().filter(e -> e.getKey().y >= startLocation.y && e.getKey().x >= startLocation.x)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        logMap(map1);
        logMap(map2);
        logMap(map3);
        logMap(map4);
        final Map<Point, Integer> keys = map.entrySet().stream()
                .filter(e -> e.getValue() >= a && e.getValue() <= z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Map<Integer, Point> keyLookup = keys.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        final Map<Point, Integer> keys1 = map1.entrySet().stream()
                .filter(e -> e.getValue() >= a && e.getValue() <= z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        final Map<Point, Integer> keys2 = map2.entrySet().stream()
                .filter(e -> e.getValue() >= a && e.getValue() <= z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        final Map<Point, Integer> keys3 = map3.entrySet().stream()
                .filter(e -> e.getValue() >= a && e.getValue() <= z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        final Map<Point, Integer> keys4 = map4.entrySet().stream()
                .filter(e -> e.getValue() >= a && e.getValue() <= z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Map<Point, Integer> doors = map.entrySet().stream()
                .filter(e -> e.getValue() >= A && e.getValue() <= Z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        RobotTSPState initialState = new RobotTSPState(
                startLocation,
                getStartingState(map1, keys1, start1),
                getStartingState(map2, keys2, start2),
                getStartingState(map3, keys3, start3),
                getStartingState(map4, keys4, start4)
        );
        Map<Point, Map<Point, DistanceToPoint>> distancesFromTo = new HashMap<>();
        distancesFromTo.putAll(getDistancesFromTo(start1, map1, keys1));
        distancesFromTo.putAll(getDistancesFromTo(start2, map2, keys2));
        distancesFromTo.putAll(getDistancesFromTo(start3, map3, keys3));
        distancesFromTo.putAll(getDistancesFromTo(start4, map4, keys4));


        Map<RobotTSPState, Integer> visitedStates = new HashMap<>();
        PriorityQueue<RobotTSPState> priorityQueueStates = new PriorityQueue<>(Comparator.comparingInt(RobotTSPState::cost));
        priorityQueueStates.add(initialState);
        while (!priorityQueueStates.isEmpty()) {
            RobotTSPState poll = priorityQueueStates.poll();
            if (poll.getKeys().isEmpty()) {
                return poll.cost();
            }
            Map<Point, List<Point>> targets = poll.targets(keyLookup);
            for (Map.Entry<Point, List<Point>> targetPair : targets.entrySet()) {
                for (Point target : targetPair.getValue()){

                    RobotTSPState next = poll.copy();
                    DistanceToPoint search = distancesFromTo.get(targetPair.getKey()).get(target);
                    next.walkTo(search);

                    Optional<Map.Entry<RobotTSPState, Integer>> any = visitedStates.entrySet().stream()
                            .filter(l -> RobotTSPState.isSameState(l.getKey(), next))
                            .findAny();
                    int cost = next.cost();
                    if (any.isPresent()) {
                        if (any.get().getValue() > cost) {
                            RobotTSPState previousKey = any.get().getKey();
                            priorityQueueStates.removeIf(s -> s.equals(previousKey));
                            visitedStates.remove(previousKey);
                            visitedStates.put(next, cost);
                            priorityQueueStates.add(next);
                        }
                    } else {
                        visitedStates.put(next, cost);
                        priorityQueueStates.add(next);
                    }
                }
            }
        }
        // 2458L is too high
        return -1;
    }

    public void readInput(String inputPath) {

        in = Input.getLines(inputPath).stream().map(s -> s.chars().boxed().collect(Collectors.toList())).collect(Collectors.toList());
    }
}
