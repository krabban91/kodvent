package krabban91.kodvent.kodvent.y2018.d12;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day12 {
    PlantGenerations in;

    public long getPart1() {
        return in.getGenerationScore(20);
    }

    public long getPart2() {
        return in.getGenerationScore(50000000000L);
    }

    public void readInput(String path) {
        this.in = new PlantGenerations(Input.getLines(path));
    }

    public Day12() {
        System.out.println("::: Starting Day 12 :::");
        String inputPath = "y2018/d12/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
