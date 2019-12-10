package krabban91.kodvent.kodvent.y2019.d11;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day11 {
    List<Integer> in;

    public Day11() {
        System.out.println("::: Starting Day 11 :::");
        String inputPath = "y2019/d11/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {

        return -1L;
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
