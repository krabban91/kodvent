package krabban91.kodvent.kodvent.day25;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class Day25 {
    private static String inputPath = "day25.txt";
    SpaceAndTime in;

    public long getPart1() {
        return in.countConstellations();
    }

    public long getPart2() {
        return -1;
    }


    public SpaceAndTime readInput(Stream<String> stream) {
        return new SpaceAndTime(stream);
    }

    public Day25() {
        System.out.println("::: Starting Day 25 :::");
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
