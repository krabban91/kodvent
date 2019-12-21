package krabban91.kodvent.kodvent.y2019.d19;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day19 {
    List<Long> in;
    private boolean debug = false;

    public Day19() throws InterruptedException {
        System.out.println("::: Starting Day 19 :::");
        String inputPath = "y2019/d19/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2500);
        Map<Point, Boolean> map = new HashMap<>();
        for (int y = 0; y < 50; y++) {
            for (int x = 0; x < 50; x++) {
                map.put(new Point(x, y), deployDrone(x, y, executor) == 1);
            }
        }
        debug(map);
        try {
            executor.awaitTermination(2L, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return map.entrySet().stream().filter(Map.Entry::getValue).count();
    }

    public long getPart2() throws InterruptedException {
        Point closestOne = new Point(0, -1);
        int leftMostBeam = 0;
        boolean foundMatch = false;
        for (int y = 0; y < 100000; y++) {
            if(foundMatch){
                break;
            }
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10000);
            for (int x = leftMostBeam; x < 1.3 * y; x++) {

                boolean isBeam = deployDrone(x, y, executor) == 1;
                if(isBeam){
                    leftMostBeam = x;
                    Point bl = new Point(x,y);
                    Point tr = new Point(x+99, y-99);
                    boolean santaFits = deployDrone(tr.x, tr.y, executor) == 1;
                    if(santaFits){
                        closestOne = new Point(bl.x, tr.y);
                        foundMatch =true;
                    }
                    break;
                }
            }
            executor.shutdown();
        }
        return closestOne.x * 10000 + closestOne.y;
    }

    private void debug(Map<Point, Boolean> map) {
        if (debug) {
            System.out.println(new LogUtils<Boolean>().mapToText(map, b -> b == null ? " " : (b ? "#" : ".")));
        }
    }

    private Long deployDrone(int x, int y, ThreadPoolExecutor executor) throws InterruptedException {
        IntCodeComputer drone = new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        executor.execute(drone);
        drone.addInput(x);
        drone.addInput(y);
        return drone.pollOutput(10);
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
