package krabban91.kodvent.kodvent.y2018.d24;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day24 {
    Battle in;

    public long getPart1() {
        return in.unitsLeftInWinningArmy();
    }

    public long getPart2() {
        return in.unitsLeftWhenGivingTheSmallestPossibleBoost();
    }


    public Battle readInput(Stream<String> stream) {
        return new Battle(stream
                .collect(Collectors.toList()));
    }

    public Day24() {
        System.out.println("::: Starting Day 24 :::");
        String inputPath = "y2018/d24/input.txt";
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            in = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
