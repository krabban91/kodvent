package krabban91.kodvent.kodvent.y2019.d02;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2 {
    List<Long> in;

    public Day2() {
        System.out.println("::: Starting Day 2 :::");
        String inputPath = "y2019/d02/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        return compute(in, 1, 12);
    }

    public long getPart2() {
        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                if (compute(in, noun, verb) == 19690720) {
                    return 100 * noun + verb;
                }
            }
        }
        return -1;
        /*
        return IntStream.rangeClosed(0, 99)
                .mapToObj(noun -> IntStream.rangeClosed(0, 99)
                        .boxed()
                        .collect(Collectors.toMap(
                                verb -> Map.entry(noun, verb),
                                verb -> compute(in, noun, verb))))
                .flatMap(l -> l.entrySet().stream())
                .filter(e -> e.getValue() == 19690720)
                .findFirst()
                .map(e -> 100 * e.getKey().getKey() + e.getKey().getValue())
                .orElse(-1);
        */

    }

    public long compute(List<Long> in, int noun, int verb) {
        IntCodeComputer computer = new IntCodeComputer(in, noun, verb);
        computer.run();
        return computer.getOutput();
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
