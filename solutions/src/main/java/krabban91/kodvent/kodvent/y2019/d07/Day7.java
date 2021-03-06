package krabban91.kodvent.kodvent.y2019.d07;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day7 {
    public static final int TRIES = 2 * 3 * 4 * 5 * 10; // markedly higher than 5!.
    List<Long> in;

    public Day7() {
        System.out.println("::: Starting Day 7 :::");
        String inputPath = "y2019/d07/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        List<Integer> phases = Arrays.asList(0, 1, 2, 3, 4);
        return IntStream.range(0, TRIES)
                .map(tests -> {
                    Collections.shuffle(phases);
                    List<IntCodeComputer> amplifiers = setUpAmplifiers(phases);
                    amplifiers.get(0).addInput(0);
                    for (int i = 0; i < 5; i++) {
                        IntCodeComputer a = amplifiers.get(i);
                        a.run();
                    }
                    return amplifiers.get(4).lastOutput().intValue();
                })
                .max()
                .orElse(-1);
    }

    public long getPart2() {
        List<Integer> phases = Arrays.asList(5, 6, 7, 8, 9);
        return LongStream.range(0, TRIES)
                .map(tests -> {
                    Collections.shuffle(phases);
                    List<IntCodeComputer> amplifiers = setUpAmplifiers(phases);
                    amplifiers.get(0).addInput(0);
                    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
                    IntStream.range(0, 5).forEach(i -> executor.execute(amplifiers.get(i)));
                    executor.shutdown();
                    try {
                        executor.awaitTermination(1L, TimeUnit.DAYS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return amplifiers.get(4).lastOutput();
                })
                .max()
                .orElse(-1);
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private List<IntCodeComputer> setUpAmplifiers(List<Integer> phases) {
        List<IntCodeComputer> computers = new ArrayList<>();
        List<Map<Integer, LinkedBlockingDeque<Long>>> inputOutput = buildInputAndOutputQueues();
        for (int i = 0; i < 5; i++) {
            int phase = phases.get(i);
            LinkedBlockingDeque<Long> input = inputOutput.get(0).get(i);
            IntCodeComputer amp = new IntCodeComputer(in, input, inputOutput.get(1).get(i));
            amp.addInput(phase);
            computers.add(amp);
        }
        return computers;
    }

    private List<Map<Integer, LinkedBlockingDeque<Long>>> buildInputAndOutputQueues() {
        Map<Integer, LinkedBlockingDeque<Long>> inputs = new HashMap<>();
        Map<Integer, LinkedBlockingDeque<Long>> outputs = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            LinkedBlockingDeque<Long> objects = new LinkedBlockingDeque<>();
            inputs.put(i, objects);
            outputs.put((i + 5 - 1) % 5, objects);
        }
        return Arrays.asList(inputs, outputs);
    }
}
