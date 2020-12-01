package krabban91.kodvent.kodvent.y2018.d17;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day17 {
    Reservoir in;

    public long getPart1() {
        return in.countFilledSquareMeters();
    }

    public long getPart2() {
        return in.countRetainedSquareMeters();
    }

    public void readInput(String path) {
        this.in = new Reservoir(Input.getLines(path).stream().map(ClayVein::new));
    }

    public Day17() {
        System.out.println("::: Starting Day 17 :::");
        String inputPath = "y2018/d17/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
