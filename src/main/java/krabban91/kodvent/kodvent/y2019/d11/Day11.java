package krabban91.kodvent.kodvent.y2019.d11;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

import java.awt.*;
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

@Component
public class Day11 {
    List<Long> in;

    public Day11() throws InterruptedException {
        System.out.println("::: Starting Day 11 :::");
        String inputPath = "y2019/d11/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() throws InterruptedException {
        LinkedBlockingDeque<Long> input = new LinkedBlockingDeque<>();
        LinkedBlockingDeque<Long> output = new LinkedBlockingDeque<>();
        IntCodeComputer brain = new IntCodeComputer(in, input, output);
        Map<Point, Boolean> painted = new HashMap<>();
        Point robotLocation = new Point(0,0);
        List<Point> directions = Arrays.asList(new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0));
        int currentDirection = 0;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        executor.execute(brain);
        while (!brain.hasHalted()){
            Boolean aBoolean ;
            if(painted.containsKey((robotLocation))){
                aBoolean = painted.get(robotLocation);
            } else {
                aBoolean = false;
            }
            input.addLast(aBoolean?1L:0L);
            Long color = output.pollLast(10L, TimeUnit.SECONDS);
            painted.putIfAbsent(robotLocation, color == 1);
            Long direction = output.pollLast(10L, TimeUnit.SECONDS);
            currentDirection= (directions.size() + currentDirection + (direction==0?-1:1))%directions.size();

            Point vector = directions.get(currentDirection);
            robotLocation = new Point(robotLocation.x+ vector.x, robotLocation.y+vector.y);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1L, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 558 is wrong
        return painted.size();
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
