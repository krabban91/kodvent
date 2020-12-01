package krabban91.kodvent.kodvent.y2018.d13;

import krabban91.kodvent.kodvent.utilities.Input;

import java.awt.*;

public class Day13 {
    RailRoad in;

    public Point getPart1() {
        return in.runUntilCollision();
    }

    public Point getPart2() {
        return in.runUntilOnlyOneRemains();
    }

    public void readInput(String path) {
        this.in = new RailRoad(Input.getLines(path));
    }

    public Day13() {
        System.out.println("::: Starting Day 13 :::");
        String inputPath = "y2018/d13/input.txt";
        readInput(inputPath);
        Point part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1.x + "," + part1.y);

        Point part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2.x + "," + part2.y);
    }
}
