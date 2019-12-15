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
    private List<Integer> directionNumbers = Arrays.asList(north, south, east, west);
    private List<Integer> cameFromNumbers = Arrays.asList(south, north, west, east);


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
        while (!computer.hasHalted()) {
            System.out.println(new LogUtils<Integer>().mapToText(map, i -> i == null? " ": (i== 0 ? "#" : ".")));
            if (map.get(location).equals(movedToOxygen)) {
                return pathBackToStart.size();
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
                pathBackToStart.pollLast();
            } else {
                final Point vector = this.directions.get(this.directionNumbers.indexOf(nextStep.get()));
                Point nextLocation = new Point(location.x+vector.x, location.y +vector.y);
                final Integer dir = this.directionNumbers.get(this.directions.indexOf(vector));
                //move
                computer.addInput(dir);
                final Integer cameFrom = this.cameFromNumbers.get(this.directions.indexOf(vector));
                pathBackToStart.addLast(cameFrom);
                final Long aLong = computer.pollOutput(10);
                if (aLong == wall) {
                    map.put(nextLocation, wall);
                    pathBackToStart.pollLast();
                } else if (aLong == movedToOxygen) {
                    map.put(nextLocation, movedToOxygen);
                    location = nextLocation;
                } else {
                    map.put(nextLocation, movedToNormal);
                    location = nextLocation;
                }
            }
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
