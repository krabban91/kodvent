package krabban91.kodvent.kodvent.y2018.d25;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day25 {
    SpaceAndTime in;

    public long getPart1() {
        return in.countConstellations();
    }

    public void readInput(String path) {
        in = new SpaceAndTime(Input.getLines(path));
    }

    public Day25() {
        System.out.println("::: Starting Day 25 :::");
        String inputPath = "y2018/d25/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
    }
}
