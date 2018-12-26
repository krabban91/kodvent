package krabban91.kodvent.kodvent.y2018.d23;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day23 {
    NanoGrid in;

    public long getPart1() {
        return in.numberOfNanobotsInRangeOfStrongest();
    }

    public long getPart2() {
        return in.distanceToBestLocationToStand();
    }


    public NanoGrid readInput(Stream<String> stream) {
        return new NanoGrid(stream);
    }

    public Day23() {
        System.out.println("::: Starting Day 23 :::");
        String inputPath = "y2018/d23/input.txt";
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
