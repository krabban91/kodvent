package krabban91.kodvent.kodvent.day6;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class Day6 {
    private static String inputPath = "day6.txt";

    public int getPart1() {
        return -1;
    }

    public int getPart2() {
        return -1;
    }

    private void readInput(Stream<String> stream) {
    }

    public Day6() {
        System.out.println("::: Starting Day 5:::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
