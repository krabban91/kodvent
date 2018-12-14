package krabban91.kodvent.kodvent.day14;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day14 {
    private static String inputPath = "day14.txt";
    RecipeMaker in;

    public String getPart1() {
        return in.generateRecipes();
    }

    public long getPart2() {
        return in.generateUntilScoreMatcherAppears();
    }


    public RecipeMaker readInput(Stream<String> stream) {
        return stream
                .map(RecipeMaker::new)
                .findAny().get();
    }

    public Day14() {
        System.out.println("::: Starting Day 14 :::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            in = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
