package krabban91.kodvent.kodvent.day16;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day16 {
    private static String inputPath = "day16.txt";
    List<String> in;

    public long getPart1() {
        return -1L;
    }

    public long getPart2() {
        return -1;
    }


    public List<String> readInput(Stream<String> stream) {
        return stream
                .map(String::new)
                .collect(Collectors.toList());
    }

    public Day16() {
        System.out.println("::: Starting Day 16 :::");
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
