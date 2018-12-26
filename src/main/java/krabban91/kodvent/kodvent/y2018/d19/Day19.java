package krabban91.kodvent.kodvent.y2018.d19;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day19 {
    private static String inputPath = "day19.txt";
    GpuUnit in;

    public long getPart1() {
        return in.valueAtRegisterAfterOperations(0);
    }

    public long getPart2() {
        return in.valueAtRegisterAfterOperationsPart2(0);
    }


    public GpuUnit readInput(Stream<String> stream) {
        return new GpuUnit(stream
                .collect(Collectors.toList()));
    }



    public Day19() {
        System.out.println("::: Starting Day 19 :::");
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
