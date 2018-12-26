package krabban91.kodvent.kodvent.y2018.d17;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day17 {
    Reservoir in;

    public long getPart1() {
        return in.countFilledSquareMeters();
    }

    public long getPart2() {
        return in.countRetainedSquareMeters();
    }

    public Reservoir readInput(Stream<String> stream) {
        return new Reservoir(stream
                .map(ClayVein::new));
    }

    public Day17() {
        System.out.println("::: Starting Day 17 :::");
        String inputPath = "y2018/d17/input.txt";
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
