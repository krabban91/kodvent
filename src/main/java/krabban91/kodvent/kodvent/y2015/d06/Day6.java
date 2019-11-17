package krabban91.kodvent.kodvent.y2015.d06;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.stream.Collectors;

public class Day6 {
    LightGrid in;

    public Day6() {
        System.out.println("::: Starting Day 6 :::");
        String inputPath = "y2015/d06/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        return in.countLitLights();
    }

    public long getPart2() {
        return in.totalBrightness();
    }

    public void readInput(String inputPath) {
        in = new LightGrid(Input.getLines(inputPath).stream().map(Instruction::new).collect(Collectors.toList()));
    }
}
