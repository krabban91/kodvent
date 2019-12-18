package krabban91.kodvent.kodvent.y2019.d18;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.Inet4Address;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day18 {
    public static final int DOT = '.';
    List<List<Integer>> in;
    private static final int WALL = 35;
    private static final int A = 65;
    private static final int a = 97;
    private static final int UPPER_TO_LOWER = a-A;
    private static final int Z = 90;
    private static final  int z = 122;
    private static final int AT = 64;
    private static final Point VEC_NORTH = new Point(0, -1);
    private static final Point VEC_EAST = new Point(1, 0);
    private static final Point VEC_SOUTH = new Point(0, 1);
    private static final Point VEC_WEST = new Point(-1, 0);
    private static final List<Point> VECTORS = Arrays.asList(VEC_NORTH, VEC_SOUTH, VEC_WEST, VEC_EAST);

    public Day18() {
        System.out.println("::: Starting Day 18 :::");
        String inputPath = "y2019/d18/input.txt";
        readInput(inputPath);
        long part1 = -1;//getPart1();
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
        final Map<Point, Integer> doors = map.entrySet().stream()
                .filter(e -> e.getValue() >= A && e.getValue() <= Z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Point startLocation = map.entrySet().stream()
                .filter(e -> e.getValue() == AT)
                .findFirst()
                .map(Map.Entry::getKey)
                .get();


        // For each key:
        // - A* from start
        //   gather the dependency graph (f-> d+e, d ->[], e-> [])

        // goal: reach dependency with most deps.
        // example above -> f needs d and e. get all with no dependencies
        // take the one with shortest total path. (
        // gives order d->e->f or e->d->f. which has least steps?


        int stepsTaken = 0;
        Point current = startLocation;
        // while keys left to find
        Map<Point, Integer> reachableMap = new HashMap<>();
        keys.forEach(reachableMap::put);
        Set<Point> visited = new HashSet<>();
        Set<Point> reachableKeys = new HashSet<>();
        Deque<Point> unVisited = new LinkedBlockingDeque<>();
        unVisited.addLast(current);
        while (!keys.isEmpty()) {
            System.out.println(new LogUtils<Integer>().mapToText(map, i-> i==null? " ": ((char)i.intValue()+"")));
            // populate reachable map
            while (!unVisited.isEmpty()){
                final Point point = unVisited.pollFirst();
                //here
                visited.add(point);
                Integer type = map.get(point);
                reachableMap.put(point, type);
                if(type>=a && type<= z){
                    reachableKeys.add(point);
                }
                if(type >= A && type <=Z){
                    continue;
                }
                //next
                VECTORS.stream().map(p -> new Point(p.x + point.x, p.y + point.y))
                        .filter(map::containsKey)
                        .filter(p -> !map.get(p).equals(WALL))
                        //.filter(p -> !(map.get(p) >= A && map.get(p)<=Z))
                        .filter(p -> !unVisited.contains(p))
                        .filter(p -> !visited.contains(p))
                        .forEach(unVisited::addLast);
            }
            // visualize the reachable surface
            System.out.println(new LogUtils<Integer>().mapToText(reachableMap, i-> i==null? " ": ((char)i.intValue()+"")));


            // find location of closest key using bfs
            Map<Point, Integer> distances = new HashMap<>();
            Deque<Point> toVisit = new LinkedBlockingDeque<>();
            toVisit.add(current);
            distances.put(current, 0);
            Point bfsPoint = current;
            while (!toVisit.isEmpty()) {
                final Point point = toVisit.pollFirst();
                bfsPoint = point;
                final Integer distanceToHere = distances.get(point);
                //here
                final Integer integer = map.get(point);
                if (keys.values().stream().anyMatch(k-> k.equals(integer))){
                    break;
                }
                    //next
                    final List<Point> collect = VECTORS.stream().map(p -> new Point(p.x + point.x, p.y + point.y))
                            .filter(p -> map.containsKey(p) && !map.get(p).equals(WALL) && !(map.get(p) >= A && map.get(p)<=Z))
                            .filter(p -> !distances.containsKey(p))
                            .filter(p -> !toVisit.contains(p))
                            .collect(Collectors.toList());
                collect.forEach(toVisit::addLast);
                collect.forEach(p -> distances.put(p, distanceToHere + 1));
            }
            // move to key location
            map.put(current, DOT);
            reachableMap.put(current, DOT);
            stepsTaken+= distances.get(bfsPoint);
            current = bfsPoint;
            reachableMap.put(current, AT);
            map.put(current, AT);
            // remove key from map
            final Integer removedKey = keys.remove(bfsPoint);
            reachableKeys.remove(bfsPoint);
            // unlock door connected to key
            final Optional<Map.Entry<Point, Integer>> removedDoor = doors.entrySet().stream().filter(i -> removedKey == i.getValue() + UPPER_TO_LOWER).findFirst();
            removedDoor.ifPresent(entry -> {
                doors.remove(entry.getKey());
                map.put(entry.getKey(), DOT);
                reachableMap.put(entry.getKey(), DOT);
                unVisited.addLast(entry.getKey());
            });
        }
        // return number of steps taken
        return stepsTaken;
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {

        in = Input.getLines(inputPath).stream().map(s -> s.chars().boxed().collect(Collectors.toList())).collect(Collectors.toList());
    }
}
