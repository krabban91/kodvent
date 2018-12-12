package krabban91.kodvent.kodvent.day13;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day13 {
    private static String inputPath = "day13.txt";
    RailRoad in;

    public Point getPart1() {
        return in.runUntilCollision();
    }

    public long getPart2() {
        return -1;
    }


    public RailRoad readInput(Stream<String> stream) {
        return new RailRoad(stream
                .collect(Collectors.toList()));
    }

    public Day13() {
        System.out.println("::: Starting Day 13 :::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            in = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Point part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
