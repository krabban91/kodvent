package krabban91.kodvent.kodvent.y2018.d22;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day22 {
    ModeMaze in;

    public long getPart1() {
        return in.estimateDangerLevel();
    }

    public long getPart2() {
        return in.fewestMinutesToReachTarget();
    }

    public void readInput(String path) {
        in = new ModeMaze(Input.getLines(path));
    }

    public Day22() {
        System.out.println("::: Starting Day 22 :::");
        String inputPath = "y2018/d22/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
