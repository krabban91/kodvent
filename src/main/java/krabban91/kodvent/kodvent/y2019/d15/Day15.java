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
    List<Long> in;
    int north = 1, south = 2, west = 3, east = 4;
    int wall = 0, movedToNormal = 1, movedToOxygen = 2;
    Point vecNorth = new Point(0, -1);
    Point vecEast = new Point(1, 0);
    Point vecSouth = new Point(0, 1);
    Point vecWest = new Point(-1, 0);
    List<Point> directions = Arrays.asList(vecNorth, vecSouth, vecWest, vecEast);
    private List<Integer> directionNumbers = Arrays.asList(north, south, west, east);
    private List<Integer> cameFromNumbers = Arrays.asList(south, north, east, west);


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
        Long stepsFromStart = -1L;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        IntCodeComputer computer = new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        executor.execute(computer);
        Map<Point, Integer> map = new HashMap<>();
        Point location = new Point(0, 0);
        Deque<Integer> pathBackToStart = new LinkedBlockingDeque<>();
        map.put(location, movedToNormal);
        Point oxygenLocation = null;
        while (!computer.hasHalted()) {
            System.out.println(new LogUtils<Integer>().mapToText(map, i -> i == null ? " " : (i == 0 ? "#" : (i == 1 ? "." : "O"))));
            if (map.get(location).equals(movedToOxygen)) {
                // we want to explore full map

            }
            final Integer directionBack = pathBackToStart.peekLast();
            Set<Integer> alternatives = new HashSet<>(this.directionNumbers);
            if (directionBack != null) {
                alternatives.remove(directionBack);
            }
            Point finalLocation = location;
            final Optional<Integer> nextStep = alternatives.stream().filter(i -> {
                final Point point = this.directions.get(this.directionNumbers.indexOf(i));
                final Point point1 = new Point(finalLocation.x + point.x, finalLocation.y + point.y);
                return !map.containsKey(point1);
            }).findFirst();
            if (nextStep.isEmpty()) {
                if (pathBackToStart.isEmpty()) {
                    // back to start, no more paths to take.
                    // oxygen Tank should have been found.
                    break;
                }
                final Integer integer = pathBackToStart.pollLast();

                final Point vector = this.directions.get(this.directionNumbers.indexOf(integer));
                Point nextLocation = new Point(location.x + vector.x, location.y + vector.y);
                computer.addInput(integer);
                location = nextLocation;
                // we already know the answer
                final Long report = computer.pollOutput(10);
            } else {
                final Integer next = nextStep.get();
                final Point vector = this.directions.get(this.directionNumbers.indexOf(next));
                Point nextLocation = new Point(location.x + vector.x, location.y + vector.y);
                final Integer cameFrom = this.cameFromNumbers.get(this.directionNumbers.indexOf(next));
                //move
                computer.addInput(next);
                pathBackToStart.addLast(cameFrom);
                final Long report = computer.pollOutput(10);
                if (report == wall) {
                    map.put(nextLocation, wall);
                    pathBackToStart.pollLast();
                } else if (report == movedToOxygen) {
                    map.put(nextLocation, movedToOxygen);
                    location = nextLocation;
                    oxygenLocation = location;
                } else if (report == movedToNormal) {
                    map.put(nextLocation, movedToNormal);
                    location = nextLocation;
                } else {
                    int a = 0;
                }
            }
        }

        if (oxygenLocation != null) {
            Point start = new Point(0, 0);
            Point target = oxygenLocation;
            Map<Point, Integer> distances = new HashMap<>();
            Deque<Point> toVisit = new LinkedBlockingDeque<>();
            toVisit.addLast(start);
            distances.put(start,0);
            while (!distances.containsKey(target)) {
                final Point point = toVisit.pollFirst();
                final Integer distanceToHere = distances.get(point);
                //here
                final Integer integer = map.get(point);
                if(integer == movedToOxygen){
                    return distanceToHere;
                }
                //next
                final List<Point> collect = directions.stream().map(p -> new Point(p.x + point.x, p.y + point.y))
                        .filter(p -> map.containsKey(p) && !map.get(p).equals(wall))
                        .filter(p -> !distances.containsKey(p))
                        .filter(p -> !toVisit.contains(p))
                        .collect(Collectors.toList());
                collect.forEach(p -> toVisit.addLast(p));
                collect.forEach(p -> distances.put(p,distanceToHere+1));
            }
            // shortest path algo to point.

            return distances.get(target);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1L, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return stepsFromStart;
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(",")).map(Long::parseLong).collect(Collectors.toList());
    }
}
