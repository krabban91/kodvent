package krabban91.kodvent.kodvent.y2019.d16;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Day16 {
    List<Something> in;

    public Day16() {
        System.out.println("::: Starting Day 16 :::");
        String inputPath = "y2019/d16/input.txt";
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

        in = Input.getLines(inputPath).stream()
                .map(Something::new)
                .collect(Collectors.toList());
    }
}
