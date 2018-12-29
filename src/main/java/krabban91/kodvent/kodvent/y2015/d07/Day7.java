package krabban91.kodvent.kodvent.y2015.d07;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.stream.Collectors;

public class Day7 {
    BobbyTablesKit in;

    public long getPart1() {
        return in.getValueOf("a");
    }

    public long getPart2() {
        return in.complexRoutine();
    }

    public void readInput(String inputPath) {
        in = new BobbyTablesKit(Input.getLines(inputPath).stream().map(Operation::new).collect(Collectors.toList()));
    }

    public Day7() {
        System.out.println("::: Starting Day 7 :::");
        String inputPath = "y2015/d07/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
