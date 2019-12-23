package krabban91.kodvent.kodvent.y2019.d23;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

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

        Long firstYTo255 = null;
        for (int i = 0; i < 50; i++) {
            IntCodeComputer current = collect.get(i);
            executor.execute(current);
            current.addInput(i);
        }
        Map<Integer, LinkedBlockingDeque<List<Long>>> packets = new HashMap<>();
        while (firstYTo255 == null) {
            for (int i = 0; i < 50; i++) {
                IntCodeComputer current = collect.get(i);
                while (current.hasOutput()) {
                    Long address = current.pollOutput(1);
                    Long x = current.pollOutput(1);
                    Long y = current.pollOutput(1);

                    packets.putIfAbsent(address.intValue(), new LinkedBlockingDeque<>());
                    packets.get(address.intValue()).addLast(Arrays.asList(x, y));
                }
            }
            for (int i = 0; i < 50; i++) {
                IntCodeComputer current = collect.get(i);
                if (packets.containsKey(i)) {
                    LinkedBlockingDeque<List<Long>> queue = packets.get(i);
                    if (queue.isEmpty()) {
                        current.addInput(-1L);
                    }
                    while (!queue.isEmpty()) {
                        List<Long> point = queue.pollFirst();
                        current.addInput(point.get(0));
                        current.addInput(point.get(1));
                    }
                } else {
                    current.addInput(-1L);
                }
            }

            if (packets.containsKey(255)) {
                LinkedBlockingDeque<List<Long>> points = packets.get(255);
                if (!points.isEmpty()) {
                    firstYTo255 = points.peekFirst().get(1);
                }
            }
        }

        try {
            executor.awaitTermination(1L, TimeUnit.SECONDS);
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
