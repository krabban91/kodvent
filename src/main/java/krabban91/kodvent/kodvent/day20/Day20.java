package krabban91.kodvent.kodvent.day20;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day20 {
    private static String inputPath = "day20.txt";
    Maze in;

    public long getPart1() {
        return in.getDistanceToFurthestRoom();
    }

    public long getPart2() {
        return in.getCountOfRoomsWithDistanceAtleast(1000);
    }


    public Maze readInput(Stream<String> stream) {
        return stream
                .map(Maze::new)
                .findAny().get();
    }

    public Day20() {
        System.out.println("::: Starting Day 20 :::");
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