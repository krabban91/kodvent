package krabban91.kodvent.kodvent.y2018.d23;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day23 {
    NanoGrid in;

    public long getPart1() {
        return in.numberOfNanobotsInRangeOfStrongest();
    }

    public long getPart2() {
        return in.distanceToBestLocationToStand();
    }

    public void readInput(String path) {
        in = new NanoGrid(Input.getLines(path));
    }

    public Day23() {
        System.out.println("::: Starting Day 23 :::");
        String inputPath = "y2018/d23/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
