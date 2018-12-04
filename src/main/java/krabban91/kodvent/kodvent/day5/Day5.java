package krabban91.kodvent.kodvent.day5;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class Day5 {

    private static String inputPath = "day5.txt";

    public Day5() {
        System.out.println("::: Starting Day 5:::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            readInput(stream);
            int part1 = getPart1();
            System.out.println(": answer to part 1 :");
            System.out.println(part1);
            int part2 = getPart2();
            System.out.println(": answer to part 2 :");
            System.out.println(part2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInput(Stream<String> stream) {

    }

    public int getPart1() {
        return -1;
    }

    public int getPart2() {
        return -1;
    }
}
