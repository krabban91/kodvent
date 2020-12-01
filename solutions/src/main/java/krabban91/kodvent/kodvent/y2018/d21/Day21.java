package krabban91.kodvent.kodvent.y2018.d21;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day21 {
    ActivationProcess in;

    public long getPart1() {
        return in.firstHaltingValue();
    }

    public long getPart2() {
        return in.lastHaltingValue();
    }

    public void readInput(String path) {
        this.in = new ActivationProcess(Input.getLines(path));
    }

    public Day21() {
        System.out.println("::: Starting Day 21 :::");
        String inputPath = "y2018/d21/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
