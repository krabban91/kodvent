package krabban91.kodvent.kodvent.y2018.d24;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day24 {
    Battle in;

    public long getPart1() {
        return in.unitsLeftInWinningArmy();
    }

    public long getPart2() {
        return in.unitsLeftWhenGivingTheSmallestPossibleBoost();
    }

    public void readInput(String path) {
        in = new Battle(Input.getLines(path));
    }

    public Day24() {
        System.out.println("::: Starting Day 24 :::");
        String inputPath = "y2018/d24/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
