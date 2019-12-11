package krabban91.kodvent.kodvent.y2019.d11;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11 {
    private static final List<Point> directions = Arrays.asList(new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0));
    List<Long> in;

    public Day11() throws InterruptedException {
        System.out.println("::: Starting Day 11 :::");
        String inputPath = "y2019/d11/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        String part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() throws InterruptedException {
        return paint(new HashMap<>()).size();
    }

    public String getPart2() throws InterruptedException {
        Map<Point, Boolean> ship = new HashMap<>();
        ship.put(new Point(0, 0), true);

        Map<Point, Boolean> painted = paint(ship);
        return new LogUtils<Boolean>().mapToText(painted, b -> b == null ? " " : ((!b ? "." : "#")));
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private Map<Point, Boolean> paint(Map<Point, Boolean> painted) throws InterruptedException {
        IntCodeComputer brain = new IntCodeComputer(new ArrayList<>(in), new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        Point robotLocation = new Point(0, 0);
        int currentDirection = 0;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        executor.execute(brain);
        while (!brain.hasHalted()) {
            brain.addInput(painted.containsKey(robotLocation) && painted.get(robotLocation) ? 1L : 0L);
            painted.put(robotLocation, brain.pollOutput(10L) == 1);
            currentDirection = rotate(currentDirection, brain.pollOutput(10L));
            robotLocation = move(robotLocation, currentDirection);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1L, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return painted;
    }

    private int rotate(int currentDirection, Long turn) {
        return (directions.size() + currentDirection + (turn == 0 ? -1 : 1)) % directions.size();
    }

    private Point move(Point current, int direction) {
        Point vector = directions.get(direction);
        return new Point(current.x + vector.x, current.y + vector.y);
    }
}
