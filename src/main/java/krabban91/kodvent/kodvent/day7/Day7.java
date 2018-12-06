package krabban91.kodvent.kodvent.day7;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day7 {
    private static String inputPath = "day7.txt";
    List<Integer> integers;

    public int getPart1() {
        return -1;
    }

    public int getPart2() {
        return -1;
    }

    public int parseRow(String row) {
        return -1;
    }

    public List<Integer> readInput(Stream<String> stream) {
        return stream.map(this::parseRow).collect(Collectors.toList());
    }

    public Day7() {
        System.out.println("::: Starting Day 7 :::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            integers = readInput(stream);
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
