package krabban91.kodvent.kodvent.y2019.d23;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class Day23 {
    List<Long> in;

    public Day23() throws InterruptedException {
        System.out.println("::: Starting Day 23 :::");
        String inputPath = "y2019/d23/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);

        Map<Integer, IntCodeComputer> collect = IntStream.range(0, 50).boxed().collect(Collectors.toMap(e -> e, e -> new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>())));

        Integer firstYTo255 = null;
        for (int i = 0; i < 50; i++) {
            IntCodeComputer current = collect.get(i);
            executor.execute(current);
            current.addInput(i);
        }
        TimeUnit.SECONDS.sleep(1);
        while (firstYTo255 == null) {
            Map<Integer, List<Point>> packets = new HashMap<>();
            while (collect.values().stream().anyMatch(i-> {
                try {
                    return i.hasOutput();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            })) {
                for (int i = 0; i < 50; i++) {
                    IntCodeComputer current = collect.get(i);
                    if (current.hasOutput()) {
                        Long address = current.pollOutput(10);
                        Long x = current.pollOutput(10);
                        Long y = current.pollOutput(10);
                        packets.putIfAbsent(address.intValue(), new ArrayList<>());
                        packets.get(address.intValue()).add(new Point(x.intValue(), y.intValue()));
                    }
                }
            }
            for (int i = 0; i < 50; i++) {
                IntCodeComputer current = collect.get(i);
                if (packets.containsKey(i)) {
                    List<Point> points = packets.get(i);
                    points.forEach(point -> {
                        current.addInput(point.x);
                        current.addInput(point.y);
                    });
                } else {
                    current.addInput(-1L);
                }
            }
            if (packets.containsKey(255)) {
                List<Point> points = packets.get(255);
                firstYTo255 = points.get(0).y;
            }
        }

        try {
            executor.awaitTermination(2L, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return firstYTo255;
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
