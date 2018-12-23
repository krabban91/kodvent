package krabban91.kodvent.kodvent.day23;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Component
public class Day23 {
    private static String inputPath = "day23.txt";
    NanoGrid in;

    public long getPart1() {
        return in.numberOfNanobotsInRangeOfStrongest();
    }

    public long getPart2() {
        return -1;
    }


    public NanoGrid readInput(Stream<String> stream) {
        return new NanoGrid(stream);
    }

    public Day23() {
        System.out.println("::: Starting Day 23 :::");
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