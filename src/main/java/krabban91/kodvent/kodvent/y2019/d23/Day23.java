package krabban91.kodvent.kodvent.y2019.d23;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        Map<Integer, IntCodeComputer> network = StartNetwork(executor);
        Map<Integer, LinkedBlockingDeque<List<Long>>> packets = new HashMap<>();
        runUntilIdle(network, packets, false);
        stopNetwork(executor);
        return packets.get(255).peekFirst().get(1);
    }

    public long getPart2() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);

        Map<Integer, IntCodeComputer> network = StartNetwork(executor);
        Set<Long> historyNatY = new HashSet<>();
        Long repeatedNatY = null;
        boolean initiated = false;
        Map<Integer, LinkedBlockingDeque<List<Long>>> packets = new HashMap<>();
        while (repeatedNatY == null) {
            runUntilIdle(network, packets, initiated);
            initiated = true;
            LinkedBlockingDeque<List<Long>> points = packets.get(255);
            List<Long> longs = points.peekLast();
            Long x = longs.get(0);
            Long y = longs.get(1);
            if (historyNatY.contains(y)) {
                repeatedNatY = y;
            } else {
                historyNatY.add(y);
                IntCodeComputer current = network.get(0);
                current.addInput(x);
                current.addInput(y);
            }
        }

        stopNetwork(executor);
        return repeatedNatY;
    }

    private Map<Integer, IntCodeComputer> StartNetwork(ThreadPoolExecutor executor) {
        Map<Integer, IntCodeComputer> collect = IntStream.range(0, 50).boxed().collect(Collectors.toMap(e -> e, e -> new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>())));
        for (int i = 0; i < 50; i++) {
            IntCodeComputer current = collect.get(i);
            executor.execute(current);
            current.addInput(i);
        }
        return collect;
    }

    private void stopNetwork(ThreadPoolExecutor executor) {
        try {
            executor.awaitTermination(1L, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runUntilIdle(Map<Integer, IntCodeComputer> collect, Map<Integer, LinkedBlockingDeque<List<Long>>> packets, boolean initiated) throws InterruptedException {
        while (!initiated ||
                packets.entrySet().stream()
                        .filter(e -> e.getKey() != 255)
                        .anyMatch(l -> !l.getValue().isEmpty()) ||
                collect.values().stream()
                        .anyMatch(IntCodeComputer::safeHasOutput)) {
            initiated = true;
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
        }
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
