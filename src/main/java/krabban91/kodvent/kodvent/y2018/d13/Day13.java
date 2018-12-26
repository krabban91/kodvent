package krabban91.kodvent.kodvent.y2018.d13;

import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day13 {
    RailRoad in;

    public Point getPart1() {
        return in.runUntilCollision();
    }

    public Point getPart2() {
        return in.runUntilOnlyOneRemains();
    }

    public RailRoad readInput(Stream<String> stream) {
        return new RailRoad(stream
                .collect(Collectors.toList()));
    }

    public Day13() {
        System.out.println("::: Starting Day 13 :::");
        String inputPath = "y2018/d13/input.txt";
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            in = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Point part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1.x + "," + part1.y);

        Point part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2.x + "," + part2.y);
    }
}
