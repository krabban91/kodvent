package krabban91.kodvent.kodvent.y2019.d05;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 {
    List<Long> in;

    public Day5() {
        System.out.println("::: Starting Day 5 :::");
        String inputPath = "y2019/d05/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        LinkedBlockingDeque<Long> input = new LinkedBlockingDeque<>();
        input.add(1L);
        IntCodeComputer intCodeComputer = new IntCodeComputer(in, input);
        intCodeComputer.run();
        return intCodeComputer.lastOutput();
    }

    public long getPart2() {
        LinkedBlockingDeque<Long> input = new LinkedBlockingDeque<>();
        input.add(5L);
        IntCodeComputer intCodeComputer = new IntCodeComputer(in, input);
        intCodeComputer.run();
        return intCodeComputer.lastOutput();
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
