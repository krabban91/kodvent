package krabban91.kodvent.kodvent.y2015.d02;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.List;
import java.util.stream.Collectors;

public class Day2 {
    List<Gift> in;

    public long getPart1() {
        return in.stream().map(Gift::wrappingPaperArea).reduce(0, Integer::sum);
    }

    public int getPart2() {
        return in.stream().map(Gift::ribbonLength).reduce(0, Integer::sum);
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).stream().map(Gift::new).collect(Collectors.toList());
    }

    public Day2() {
        System.out.println("::: Starting Day 2 :::");
        String inputPath = "y2015/d02/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
