package krabban91.kodvent.kodvent.y2019.d09;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9 {
    List<Long> in;

    public Day9() {
        System.out.println("::: Starting Day 9 :::");
        String inputPath = "y2019/d09/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        IntCodeComputer intCodeComputer = new IntCodeComputer(in, new LinkedBlockingDeque<>(Collections.singletonList(1L)));
        intCodeComputer.run();
        return intCodeComputer.lastOutput();
    }

    public long getPart2() {
        IntCodeComputer intCodeComputer = new IntCodeComputer(in, new LinkedBlockingDeque<>(Collections.singletonList(2L)));
        intCodeComputer.run();
        return intCodeComputer.lastOutput();
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
