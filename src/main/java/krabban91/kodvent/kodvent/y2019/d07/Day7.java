package krabban91.kodvent.kodvent.y2019.d07;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day7 {
    List<Integer> in;

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
        return -1;
        /*
        List<Integer> phases = Arrays.asList(0, 1, 2, 3, 4);
        int largestOutput = 0;

        for (int tests = 0; tests < 10000; tests++) {
            Set<Integer> usedPhases = new HashSet<>();
            Collections.shuffle(phases);
            int previousOutput = 0;
            for (int i = 0; i < 5; i++) {
                List<Integer> collect = phases.stream().filter(e -> !usedPhases.contains(e)).collect(Collectors.toList());
                int phase = collect.get(0);
                usedPhases.add(phase);
                IntCodeComputer a = new IntCodeComputer(in, new LinkedList<>(Arrays.asList(phase, previousOutput)));
                a.run();
                previousOutput = a.lastOutput();
            }
            largestOutput = Math.max(previousOutput, largestOutput);
            System.out.println(largestOutput);
        }


        return largestOutput;
            */
    }

    public long getPart2() {
        List<Integer> phases = Arrays.asList(9, 8, 7, 6, 5);
        int largestOutput = 0;

        for (int tests = 0; tests < 10000; tests++) {
            Set<Integer> usedPhases = new HashSet<>();
            Collections.shuffle(phases);
            int previousOutput = 0;
            List<IntCodeComputer> computers = new ArrayList<>();
            LinkedList[] inputs = new LinkedList[5];
            LinkedList[] outputs = new LinkedList[5];
            for(int i=0; i< 5; i++){
                LinkedList<Integer> objects = new LinkedList<>();
                inputs[i] = objects;
                outputs[(i+5-1)%5] = objects;
            }
            for (int i = 0; i < 5; i++) {
                List<Integer> collect = phases.stream().filter(e -> !usedPhases.contains(e)).collect(Collectors.toList());
                int phase = collect.get(0);
                usedPhases.add(phase);
                LinkedList input = inputs[i];
                input.add(phase);
                IntCodeComputer a = new IntCodeComputer(in, input, outputs[i]);
                computers.add(a);

            }
            computers.get(0).addInput(0);
            for (int i = 0; i < 4; i++) {
                IntCodeComputer a = computers.get(i);
                a.runUntilOutputSize(2);
            }
            IntCodeComputer a = computers.get(4);
            a.runUntilOutputSize(1);
            boolean halted = false;
            while (!halted) {
                for (int i = 0; i < 5; i++) {
                    a = computers.get(i);
                    a.runUntilOutputSize(1);
                    Integer previousOutput1 = a.lastOutput();
                    if (a.hasHalted()) {
                        halted = true;
                    }
                    previousOutput = previousOutput1;
                        //computers.get((i + 1) % 5).addInput(previousOutput);

                }
            }

            largestOutput = Math.max(previousOutput, largestOutput);
            System.out.println(largestOutput);
        }


        return largestOutput;
    }

    public void readInput(String inputPath) {

        in = Stream.of(Input.getSingleLine(inputPath).split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }
}
