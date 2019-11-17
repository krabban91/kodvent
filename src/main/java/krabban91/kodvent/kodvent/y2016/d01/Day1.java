package krabban91.kodvent.kodvent.y2016.d01;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Day1 {

    List<String> instructions;

    public Day1() {
        System.out.println("::: Starting Day 1 :::");
        String inputPath = "y2016/d01/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        Taxi taxi = new Taxi();
        instructions.stream().forEachOrdered(taxi::move);
        return taxi.distanceFromStart();
    }

    public long getPart2() {
        Taxi taxi = new Taxi();
        instructions.stream().forEachOrdered(taxi::move);
        return taxi.distanceFromStartToFirstDoubleVisit();
    }

    public void readInput(String inputPath) {
        instructions = Arrays.asList(Input.getSingleLine(inputPath).split(", "));
    }
}
