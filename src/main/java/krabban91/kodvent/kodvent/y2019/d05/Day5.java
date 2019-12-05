package krabban91.kodvent.kodvent.y2019.d05;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day5 {
    List<Integer> in;

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
        LinkedList<Integer> input = new LinkedList<>();
        input.add(1);
        IntCodeComputer intCodeComputer = new IntCodeComputer(in, input);
        intCodeComputer.run();
        return intCodeComputer.lastOutput();
    }

    public long getPart2() {
        LinkedList<Integer> input = new LinkedList<>();
        input.add(5);
        IntCodeComputer intCodeComputer = new IntCodeComputer(in, input);
        intCodeComputer.run();
        return intCodeComputer.lastOutput();
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
