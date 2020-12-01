package krabban91.kodvent.kodvent.y2015.d08;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.stream.Collectors;

public class Day8 {
    Parser in;

    public long getPart1() {
        return in.totalCodeCharacters() - in.totalInMemoryCharacters();
    }

    public long getPart2() {
        return in.totalSizeOfEncodedCharacters() - in.totalCodeCharacters();
    }

    public void readInput(String inputPath) {
        in = new Parser(Input.getLines(inputPath).stream().map(Loc::new).collect(Collectors.toList()));
    }

    public Day8() {
        System.out.println("::: Starting Day 8 :::");
        String inputPath = "y2015/d08/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
