package krabban91.kodvent.kodvent.y2019.d07;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        return IntStream.range(0, TRIES)
                .map(tests -> {
                    Collections.shuffle(phases);
                    List<IntCodeComputer> amplifiers = setUpAmplifiers(phases);
                    amplifiers.get(0).addInput(0);
                    for (int i = 0; i < 4; i++) {
                        IntCodeComputer a = amplifiers.get(i);
                        a.runUntilOutputSize(2);
                    }
                    IntCodeComputer amp = amplifiers.get(4);
                    amp.runUntilOutputSize(1);
                    boolean halted = false;
                    while (!halted) {
                        for (int i = 0; i < 5; i++) {
                            amp = amplifiers.get(i);
                            amp.runUntilOutputSize(1);
                            if (amp.hasHalted()) {
                                halted = true;
                            }
                        }
                    }
                    return amplifiers.get(4).lastOutput().intValue();
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
        List<Map<Integer, Deque<Long>>> inputOutput = buildInputAndOutputQueues();
        for (int i = 0; i < 5; i++) {
            int phase = phases.get(i);
            Deque<Long> input = inputOutput.get(0).get(i);
            IntCodeComputer amp = new IntCodeComputer(in, input, inputOutput.get(1).get(i));
            amp.addInput(phase);
            computers.add(amp);
        }
        return computers;
    }

    private List<Map<Integer, Deque<Long>>> buildInputAndOutputQueues() {
        Map<Integer, Deque<Long>> inputs = new HashMap<>();
        Map<Integer, Deque<Long>> outputs = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            LinkedList<Long> objects = new LinkedList<>();
            inputs.put(i, objects);
            outputs.put((i + 5 - 1) % 5, objects);
        }
        return Arrays.asList(inputs, outputs);
    }
}
