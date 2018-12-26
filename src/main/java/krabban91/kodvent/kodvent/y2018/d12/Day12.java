package krabban91.kodvent.kodvent.y2018.d12;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day12 {
    private static String inputPath = "day12.txt";
    PlantGenerations in;

    public long getPart1() {
        return in.getGenerationScore(20);
    }

    public long getPart2() {
        return in.getGenerationScore(50000000000L);
    }

    public PlantGenerations readInput(Stream<String> stream) {
        return new PlantGenerations(stream);
    }

    public Day12() {
        System.out.println("::: Starting Day 12 :::");
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
