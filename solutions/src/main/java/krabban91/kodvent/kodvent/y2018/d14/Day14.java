package krabban91.kodvent.kodvent.y2018.d14;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day14 {
    RecipeMaker in;

    public String getPart1() {
        return in.generateRecipes();
    }

    public long getPart2() {
        return in.generateUntilScoreMatcherAppears();
    }

    public void readInput(String path) {
        this.in = new RecipeMaker(Input.getSingleLine(path));
    }

    public Day14() {
        System.out.println("::: Starting Day 14 :::");
        String inputPath = "y2018/d14/input.txt";
        readInput(inputPath);
        String part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
