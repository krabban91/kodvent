package krabban91.kodvent.kodvent.y2019.d19;

import krabban91.kodvent.kodvent.utilities.Input;
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
                IntCodeComputer drone = new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
                executor.execute(drone);
                drone.addInput(x);
                drone.addInput(y);
                map.put(new Point(x, y), drone.pollOutput(10) == 1);
            }
        }
        System.out.println(new LogUtils<Boolean>().mapToText(map, b -> b == null ? " " : (b ? "#" : ".")));
        try {
            executor.awaitTermination(2L, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return map.entrySet().stream().filter(Map.Entry::getValue).count();
    }

    public long getPart2() throws InterruptedException {
        Map<Point, Boolean> map = new HashMap<>();
        Point closestOne = null;

        for (int y = 0; y < 100000; y++) {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10000);
            for (int x = y; x < 1.3 * y; x++) {
                IntCodeComputer drone = new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
                executor.execute(drone);
                drone.addInput(x);
                drone.addInput(y);

                map.put(new Point(x, y), drone.pollOutput(10) == 1);
            }
            executor.shutdown();
            int finalY = y;
            long count = map.entrySet().stream()
                    .filter(e -> e.getKey().y == finalY)
                    .filter(Map.Entry::getValue).count();
            if (count > 100) {
                Optional<Point> first = map.entrySet().stream()
                        .filter(e -> e.getKey().y == finalY)
                        .filter(Map.Entry::getValue)
                        .max(Comparator.comparingInt(e -> e.getKey().x))
                        .map(Map.Entry::getKey);
                if (first.isPresent()) {
                    Point br = first.get();
                    Point tr = new Point(br.x, br.y - 99);
                    Boolean topRight = map.get(tr);
                    Point tl = new Point(br.x - 99, br.y - 99);
                    Boolean topLeft = map.get(tl);
                    Point bl = new Point(br.x - 99, br.y);
                    int width = 1 + (br.x - bl.x);
                    Boolean bottomLeft = map.get(bl);
                    System.out.println(new LogUtils<Boolean>().mapToText(map, b -> b == null ? " " : (b ? "#" : ".")));
                    if (topRight != null && topRight && topLeft != null && topLeft && bottomLeft != null && bottomLeft) {
                        closestOne = tl;
                        break;
                    }
                }
            }
        }
        // 9490831 is wrong
        return closestOne.x * 10000 + closestOne.y;
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
