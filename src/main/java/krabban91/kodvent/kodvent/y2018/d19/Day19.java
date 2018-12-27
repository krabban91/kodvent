package krabban91.kodvent.kodvent.y2018.d19;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day19 {
    GpuUnit in;

    public long getPart1() {
        return in.valueAtRegisterAfterOperations(0);
    }

    public long getPart2() {
        return in.valueAtRegisterAfterOperationsPart2(0);
    }

    public void readInput(String path) {
        this.in = new GpuUnit(Input.getLines(path));
    }

    public Day19() {
        System.out.println("::: Starting Day 19 :::");
        String inputPath = "y2018/d19/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
