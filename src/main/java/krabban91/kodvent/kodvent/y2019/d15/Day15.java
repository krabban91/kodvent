package krabban91.kodvent.kodvent.y2019.d15;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day15 {
    private static final boolean DEBUG = false;
    private static final int NORTH = 1, SOUTH = 2, WEST = 3, EAST = 4;
    private static final int WALL = 0, NORMAL = 1, OXYGEN = 2;
    private static final Point VEC_NORTH = new Point(0, -1);
    private static final Point VEC_EAST = new Point(1, 0);
    private static final Point VEC_SOUTH = new Point(0, 1);
    private static final Point VEC_WEST = new Point(-1, 0);
    private static final List<Point> VECTORS = Arrays.asList(VEC_NORTH, VEC_SOUTH, VEC_WEST, VEC_EAST);
    private static final List<Integer> DIRECTIONS = Arrays.asList(NORTH, SOUTH, WEST, EAST);
    private static final  List<Integer> CAME_FROM_DIRECTIONS = Arrays.asList(SOUTH, NORTH, EAST, WEST);
    List<Long> in;

    public Day15() throws InterruptedException {
        System.out.println("::: Starting Day 15 :::");
        String inputPath = "y2019/d15/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        IntCodeComputer computer = new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        executor.execute(computer);
        Map<Point, Integer> map = generateMap(computer);

        final Optional<Point> oxygenLocation = map.entrySet().stream().filter(e -> e.getValue() == Day15.OXYGEN).findFirst().map(Map.Entry::getKey);
        if (oxygenLocation.isPresent()) {
            Point start = new Point(0, 0);
            Point target = oxygenLocation.get();
            Map<Point, Integer> distances = new HashMap<>();
            Deque<Point> toVisit = new LinkedBlockingDeque<>();
            toVisit.addLast(start);
            distances.put(start, 0);
            while (!distances.containsKey(target)) {
                final Point point = toVisit.pollFirst();
                final Integer distanceToHere = distances.get(point);
                //here
                final Integer integer = map.get(point);
                if (integer == Day15.OXYGEN) {
                    return distanceToHere;
                }
                //next
                final List<Point> collect = VECTORS.stream().map(p -> new Point(p.x + point.x, p.y + point.y))
                        .filter(p -> map.containsKey(p) && !map.get(p).equals(WALL))
                        .filter(p -> !distances.containsKey(p))
                        .filter(p -> !toVisit.contains(p))
                        .collect(Collectors.toList());
                collect.forEach(p -> toVisit.addLast(p));
                collect.forEach(p -> distances.put(p, distanceToHere + 1));
            }
            return distances.get(target);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1L, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long getPart2() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        IntCodeComputer computer = new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        executor.execute(computer);
        Map<Point, Integer> map = generateMap(computer);
        final Optional<Point> oxygenLocation = map.entrySet().stream().filter(e -> e.getValue() == Day15.OXYGEN).findFirst().map(Map.Entry::getKey);
        if (oxygenLocation.isPresent()) {
            int minutesPassed = -1;
            Point start = oxygenLocation.get();
            Map<Integer, Set<Point>> toVisit = new HashMap<>();
            toVisit.put(0, Set.of(start));
            while (map.values().stream().anyMatch(i -> i == NORMAL)) {
                minutesPassed++;
                final Set<Point> points = toVisit.get(minutesPassed);
                for (Point current : points) {
                    //here
                    map.put(current, Day15.OXYGEN);

                    //next
                    final int nextMinute = minutesPassed + 1;
                    final List<Point> collect = VECTORS.stream().map(p -> new Point(p.x + current.x, p.y + current.y))
                            .filter(p -> map.containsKey(p) && !map.get(p).equals(WALL))
                            .filter(p -> toVisit.entrySet().stream().noneMatch(s -> s.getValue().contains(p)))
                            .collect(Collectors.toList());
                    toVisit.putIfAbsent(nextMinute, new HashSet<>());
                    collect.forEach(p -> toVisit.computeIfPresent(nextMinute, (k, v) -> {
                        v.add(p);
                        return v;
                    }));
                }
                drawMap(map);
            }
            return minutesPassed;
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1L, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Map<Point, Integer> generateMap(IntCodeComputer computer) throws InterruptedException {
        Map<Point, Integer> map = new HashMap<>();
        Point location = new Point(0, 0);
        Deque<Integer> pathBackToStart = new LinkedBlockingDeque<>();
        map.put(location, NORMAL);
        while (!computer.hasHalted()) {
            drawMap(map);
            map.get(location);// we want to explore full map
            final Integer directionBack = pathBackToStart.peekLast();
            Set<Integer> alternatives = new HashSet<>(DIRECTIONS);
            if (directionBack != null) {
                alternatives.remove(directionBack);
            }
            Point finalLocation = location;
            final Optional<Integer> nextStep = alternatives.stream().filter(i -> {
                final Point point = VECTORS.get(DIRECTIONS.indexOf(i));
                final Point point1 = new Point(finalLocation.x + point.x, finalLocation.y + point.y);
                return !map.containsKey(point1);
            }).findFirst();
            if (nextStep.isEmpty()) {
                if (pathBackToStart.isEmpty()) {
                    // back to start, no more paths to take.
                    // OXYGEN Tank should have been found.
                    break;
                }
                final Integer integer = pathBackToStart.pollLast();

                final Point vector = VECTORS.get(DIRECTIONS.indexOf(integer));
                Point nextLocation = new Point(location.x + vector.x, location.y + vector.y);
                computer.addInput(integer);
                location = nextLocation;
                // we already know the answer
                final Long report = computer.pollOutput(10);
            } else {
                final Integer next = nextStep.get();
                final Point vector = VECTORS.get(DIRECTIONS.indexOf(next));
                Point nextLocation = new Point(location.x + vector.x, location.y + vector.y);
                final Integer cameFrom = CAME_FROM_DIRECTIONS.get(DIRECTIONS.indexOf(next));
                //move
                computer.addInput(next);
                pathBackToStart.addLast(cameFrom);
                final Long report = computer.pollOutput(10);
                if (report == WALL) {
                    map.put(nextLocation, WALL);
                    pathBackToStart.pollLast();
                } else if (report == OXYGEN) {
                    map.put(nextLocation, OXYGEN);
                    location = nextLocation;
                } else if (report == NORMAL) {
                    map.put(nextLocation, NORMAL);
                    location = nextLocation;
                }
            }
        }
        return map;
    }

    private void drawMap(Map<Point, Integer> map) {
        if (DEBUG) {
            System.out.println(new LogUtils<Integer>().mapToText(map, i -> i == null ? " " : (i == 0 ? "#" : (i == 1 ? "." : "O"))));
        }
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(",")).map(Long::parseLong).collect(Collectors.toList());
    }
}
