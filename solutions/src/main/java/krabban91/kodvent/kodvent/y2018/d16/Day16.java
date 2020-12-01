package krabban91.kodvent.kodvent.y2018.d16;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day16 {
    Processor in;

    public long getPart1() {
        return in.numberOfSamplesMatchingMoreThanOrEqualTo(3);
    }

    public long getPart2() {
        return in.valueAtRegisterAfterOperations(0);
    }

    public void readInput(String path) {
        this.in = new Processor(Input.getLines(path));
    }

    public Day16() {
        System.out.println("::: Starting Day 16 :::");
        String inputPath = "y2018/d16/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
