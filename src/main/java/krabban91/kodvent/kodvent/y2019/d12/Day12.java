package krabban91.kodvent.kodvent.y2019.d12;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.List;

//@Component
public class Day12 {
    List<String> in;

    public long getPart1() {
        return -1L;
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath);
    }

    public Day12() {
        System.out.println("::: Starting Day 12 :::");
        String inputPath = "y2019/d12/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
