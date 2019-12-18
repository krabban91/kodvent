package krabban91.kodvent.kodvent.y2019.d18;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day18 {
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
        final Map<Point, Integer> doors = map.entrySet().stream()
                .filter(e -> e.getValue() >= A && e.getValue() <= Z)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Point startLocation = map.entrySet().stream()
                .filter(e -> e.getValue() == AT)
                .findFirst()
                .map(Map.Entry::getKey)
                .get();

        int stepsTaken = 0;
        Point current = startLocation;
        // while keys left to find
        while (!keys.isEmpty()) {
            System.out.println(new LogUtils<Integer>().mapToText(map, i-> i==null? " ": ((char)i.intValue()+"")));
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
            map.put(current, (int)'.');
            stepsTaken+= distances.get(bfsPoint);
            current = bfsPoint;
            map.put(current, (int)'@');
            // remove key from map
            final Integer removedKey = keys.remove(bfsPoint);
            // unlock door connected to key
            final Optional<Map.Entry<Point, Integer>> removedDoor = doors.entrySet().stream().filter(i -> removedKey == i.getValue() + UPPER_TO_LOWER).findFirst();
            removedDoor.ifPresent(entry -> {
                doors.remove(entry.getKey());
                map.put(entry.getKey(), (int)'.');
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
