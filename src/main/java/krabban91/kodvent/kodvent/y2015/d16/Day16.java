package krabban91.kodvent.kodvent.y2015.d16;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.List;
import java.util.stream.Collectors;

public class Day16 {
    List<Aunt> in;

    public Day16() {
        System.out.println("::: Starting Day 16 :::");
        String inputPath = "y2015/d16/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        return in.stream()
                .filter(e -> e.matchChildren(3))
                .filter(e -> e.matchCats(7))
                .filter(e -> e.matchSamoyeds(2))
                .filter(e -> e.matchPomeranians(3))
                .filter(e -> e.matchAkitas(0))
                .filter(e -> e.matchVizslas(0))
                .filter(e -> e.matchGoldfish(5))
                .filter(e -> e.matchTrees(3))
                .filter(e -> e.matchCars(2))
                .filter(e -> e.matchPerfumes(1))
                .mapToLong(Aunt::getNumber)
                .findFirst().orElse(-1);
    }

    public long getPart2() {
        return in.stream()
                .filter(e -> e.matchChildren(3))
                .filter(e -> e.greaterThanCats(7))
                .filter(e -> e.matchSamoyeds(2))
                .filter(e -> e.lessThanPomeranians(3))
                .filter(e -> e.matchAkitas(0))
                .filter(e -> e.matchVizslas(0))
                .filter(e -> e.lessThanGoldfish(5))
                .filter(e -> e.greaterThanTrees(3))
                .filter(e -> e.matchCars(2))
                .filter(e -> e.matchPerfumes(1))
                .mapToLong(Aunt::getNumber)
                .findFirst().orElse(-1);
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath)
                .stream()
                .map(Aunt::new)
                .collect(Collectors.toList());
    }

}
