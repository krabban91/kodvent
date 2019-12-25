package krabban91.kodvent.kodvent.y2019.d18;

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
        Map<Map.Entry<Point, Integer>, DistanceToPoint> pathsToKeys = keys.entrySet().stream().collect(Collectors.toMap(e -> e, e -> {
            Map<Point, DistanceToPoint> checked = new HashMap<>();
            PriorityQueue<DistanceToPoint> unChecked = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint::heuristic));
            unChecked.add(new DistanceToPoint(startLocation, e.getKey()));
            Graph<DistanceToPoint, Step, Point> graph = new Graph<>();
            return graph.search(unChecked, (p, net) -> this.addNext(p, net, checked, unChecked), network);
        }));

        Map<Point, Map<Point, DistanceToPoint>> distancesFromTo = new HashMap<>();

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
        Map<List<Integer>, Map<Integer, Set<Integer>>> differentDependencies = new HashMap<>();
        Map<List<Integer>, LinkedList<DistanceToPoint>> differentPaths = new HashMap<>();
        PriorityQueue<Map.Entry<List<Integer>, LinkedList<DistanceToPoint>>> priorityQueuePaths = new PriorityQueue<>(Comparator.comparingInt(e -> e.getValue().getLast().heuristic()));

        Map<List<Integer>, Map<Point, Integer>> differentMaps = new HashMap<>();
        Map<List<Integer>, Map<Point, Integer>> differentKeys = new HashMap<>();
        Map<List<Integer>, Map<Point, Integer>> differentDoors = new HashMap<>();

        differentDependencies.put(Collections.emptyList(), deepCopyDependencies(dependencyGraph));
        priorityQueuePaths.add(Map.entry(Collections.emptyList(), new LinkedList<>(Collections.singletonList(new DistanceToPoint(startLocation, startLocation)))));
        differentPaths.put(Collections.emptyList(), new LinkedList<>(Collections.singletonList(new DistanceToPoint(startLocation, startLocation))));
        differentMaps.put(Collections.emptyList(), new HashMap<>(map));
        differentKeys.put(Collections.emptyList(), new HashMap<>(keys));
        differentDoors.put(Collections.emptyList(), new HashMap<>(doors));
        Map<Set<Integer>, Map<Point, Collection<Step>>> differentNetworks = new HashMap<>();

        stepsTaken = Integer.MAX_VALUE;
        int timesSinceChange = 0;
        while (differentKeys.values().stream().anyMatch(m -> !m.isEmpty())) {
            // goal: reach dependency with most deps.
            // example above -> f needs d and e. get all with no dependencies
            Map<List<Integer>, LinkedList<DistanceToPoint>> finalDifferentPaths = differentPaths;
            if (priorityQueuePaths.isEmpty()) {
                break;
            }
            Map.Entry<List<Integer>, LinkedList<DistanceToPoint>> poll = priorityQueuePaths.poll();
            List<Integer> mapKey = poll.getKey();
            //Set<Integer> mapKey = differentMaps.keySet().stream().min(Comparator.comparingLong(s -> differentKeys.get(s).size()).thenComparing(s -> finalDifferentPaths.get(s).stream().mapToInt(DistanceToPoint::cost).sum())).get();
            //Set<Integer> mapKey = differentMaps.keySet().stream().min(Comparator.comparingLong(s -> finalDifferentPaths.get(s).stream().mapToInt(DistanceToPoint::cost).sum())).get();
            List<Integer> localishMapKey = new ArrayList<>(mapKey);
            int stepsTakenSoFar = finalDifferentPaths.get(mapKey).stream().mapToInt(DistanceToPoint::cost).sum();
            Map<Point, Integer> localishKeys = differentKeys.get(localishMapKey);
            if (stepsTakenSoFar > stepsTaken) {
                differentMaps.remove(mapKey);
                differentPaths.remove(mapKey);
                differentDependencies.remove(mapKey);
                differentDoors.remove(mapKey);
                differentKeys.remove(mapKey);
                if (++timesSinceChange > 100000) {
                    return stepsTaken;
                }
                continue;
            }
            if (localishKeys.isEmpty()) {
                int sum = finalDifferentPaths.get(mapKey).stream().mapToInt(DistanceToPoint::cost).sum();
                if (stepsTaken <= stepsTakenSoFar) {
                    if (++timesSinceChange > 100000) {
                        return stepsTaken;
                    }
                } else {
                    stepsTaken = stepsTakenSoFar;
                    timesSinceChange = 0;
                }
                differentMaps.remove(mapKey);
                differentPaths.remove(mapKey);
                differentDependencies.remove(mapKey);
                differentDoors.remove(mapKey);
                differentKeys.remove(mapKey);
                continue;
            }

            Map<Integer, Set<Integer>> localishDependencies = deepCopyDependencies(differentDependencies.get(localishMapKey));


            List<Point> targets = localishDependencies.entrySet().stream()
                    .filter(e -> e.getValue().isEmpty())
                    .map(e -> keyLookup.get(e.getKey()))
                    .collect(Collectors.toList());
            LinkedList<DistanceToPoint> distanceToPoints = differentPaths.get(localishMapKey);
            Map<Point, Integer> pointIntegerMap = differentMaps.get(localishMapKey);
            Map<Point, Integer> pointIntegerMap1 = differentDoors.get(localishMapKey);
            for (Point target : targets) {

                Map<Integer, Set<Integer>> localDependencies = deepCopyDependencies(localishDependencies);
                List<Integer> localMapKey = new ArrayList<>(localishMapKey);
                HashMap<Point, Integer> localKeys = new HashMap<>(localishKeys);
                LinkedList<DistanceToPoint> localPaths = new LinkedList<>(distanceToPoints);
                Point current = localPaths.getLast().destination();
                distancesFromTo.putIfAbsent(current, new HashMap<>());
                Map<Point, Integer> localDoors = new HashMap<>(pointIntegerMap1);

                Map<Point, Integer> localMap = new HashMap<>(pointIntegerMap);
                // take the one with shortest total path. (
                // gives order d->e->f or e->d->f. which has least steps?

                if (!distancesFromTo.get(current).containsKey(target)) {
                    HashSet<Integer> networkKey = new HashSet<>(localMapKey);
                    differentNetworks.computeIfAbsent(networkKey, key -> getNetwork(localMap, false));

                    Map<Point, Collection<Step>> networkWithDoors = differentNetworks.get(networkKey);
                    Map<Point, DistanceToPoint> checked = new HashMap<>();
                    PriorityQueue<DistanceToPoint> unChecked = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint::heuristic));
                    unChecked.add(new DistanceToPoint(current, target));
                    Graph<DistanceToPoint, Step, Point> graph = new Graph<>();
                    distancesFromTo.get(current).put(target, graph.search(unChecked, (p, net) -> this.addNext(p, net, checked, unChecked), networkWithDoors, p -> false));
                }
                DistanceToPoint search = distancesFromTo.get(current).get(target);

                // remove key from map
                // pick up all keys that I pass by.
                List<Point> pickedUpKeys = search.allVisited().stream().filter(localKeys::containsKey)
                        .collect(Collectors.toList());
                pickedUpKeys.forEach(p -> localMap.put(p, DOT));
                // move
                // move to key location
                localMap.put(current, DOT);
                current = search.destination();
                localMap.put(current, AT);
                List<Integer> keysInPath = pickedUpKeys.stream().map(localKeys::remove).collect(Collectors.toList());
                // TODO: Setup a heuristic that is useful
                DistanceToPoint searchCopy = new DistanceToPoint(search,
                        () -> localKeys.values().size() * (maxX / 2 + maxY / 2));
                localPaths.addLast(searchCopy);
                keysInPath.forEach(removedKey -> {
                    localDependencies.remove(removedKey);
                    localDependencies.forEach((i, s) -> s.remove(removedKey));
                    // unlock door connected to key
                    localDoors.entrySet().stream()
                            .filter(i -> removedKey == i.getValue() + UPPER_TO_LOWER)
                            .findFirst().ifPresent(entry -> {
                        localDoors.remove(entry.getKey());
                        localMap.put(entry.getKey(), DOT);
                    });
                });
                localMapKey.addAll(keysInPath);
                if (differentMaps.containsKey(localMapKey)) {
                    List<DistanceToPoint> other = differentPaths.get(localMapKey);
                    int otherCost = other.stream().mapToInt(DistanceToPoint::cost).sum();
                    int localCost = localPaths.stream().mapToInt(DistanceToPoint::cost).sum();
                    if (localCost > otherCost) {
                        System.out.println("strange");
                        continue;
                    }
                }
                differentDependencies.remove(mapKey);
                differentDependencies.put(localMapKey, localDependencies);

                differentDoors.remove(mapKey);
                differentDoors.put(localMapKey, localDoors);
                differentKeys.remove(mapKey);
                differentKeys.put(localMapKey, localKeys);
                differentPaths.remove(mapKey);
                if (!differentPaths.containsKey(localMapKey)) {
                    differentPaths.put(localMapKey, localPaths);
                    priorityQueuePaths.add(Map.entry(localMapKey, localPaths));
                }
                differentMaps.remove(mapKey);
                differentMaps.put(localMapKey, localMap);
                logMap(localMap);
            }
            //int maxCollectedKeys = newKeys.keySet().stream().mapToInt(List::size).max().orElse(0);


        }

        if (stepsTaken == Integer.MAX_VALUE) {
            return differentPaths.values().stream().mapToLong(l -> l.stream().mapToInt(DistanceToPoint::cost).sum()).min().orElse(-1);
        }
        // return number of steps taken
        // 6534 is too high. 5680 is wrong, 5636 is wrong
        return stepsTaken;
    }

    private Map<Integer, Set<Integer>> deepCopyDependencies(Map<Integer, Set<Integer>> dependencies) {
        return dependencies.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new HashSet<>(e.getValue())));
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
