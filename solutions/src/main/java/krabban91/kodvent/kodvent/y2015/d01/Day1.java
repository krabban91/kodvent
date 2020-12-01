package krabban91.kodvent.kodvent.y2015.d01;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.concurrent.atomic.AtomicInteger;

//@Component
public class Day1 {
    String in;

    public long getPart1() {
        return in.chars().map(c -> c == (int) '(' ? 1 : -1).reduce(0, Integer::sum);
    }

    public int getPart2() {
        AtomicInteger floor = new AtomicInteger(0);
        AtomicInteger position = new AtomicInteger(0);
        in.chars().forEach(c -> {
            if (floor.get() >= 0) {
                floor.addAndGet(c == (int) '(' ? 1 : -1);
                position.incrementAndGet();
            }
        });
        return position.get();
    }

    public void readInput(String inputPath) {
        in = Input.getSingleLine(inputPath);
    }

    public Day1() {
        System.out.println("::: Starting Day 1 :::");
        String inputPath = "y2015/d01/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
