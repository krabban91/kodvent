package krabban91.kodvent.kodvent.y2015.d09;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.stream.Collectors;

public class Day9 {
    private PathPlanner in;

    public long getPart1() {
        return in.shortestRoute().cost();
    }

    public long getPart2() {
        return in.longestRoute().cost();
    }

    public void readInput(String inputPath) {
        in = new PathPlanner(Input.getLines(inputPath).stream().map(Road::new).collect(Collectors.toList()));
    }

    public Day9() {
        System.out.println("::: Starting Day 9 :::");
        String inputPath = "y2015/d09/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
