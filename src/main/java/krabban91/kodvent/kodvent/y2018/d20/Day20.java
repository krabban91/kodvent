package krabban91.kodvent.kodvent.y2018.d20;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day20 {
    Maze in;

    public long getPart1() {
        return in.getDistanceToFurthestRoom();
    }

    public long getPart2() {
        return in.getCountOfRoomsWithDistanceAtleast(1000);
    }

    public void readInput(String path) {
        this.in = new Maze(Input.getSingleLine(path));
    }

    public Day20() {
        System.out.println("::: Starting Day 20 :::");
        String inputPath = "y2018/d20/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
