package krabban91.kodvent.kodvent.day15;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day15 {
    private static String inputPath = "day15.txt";
    CaveBattle in;

    public long getPart1() {
        return in.battleUntilItIsOver();
    }

    public long getPart2(String inputPath) {
        long elfCount = in.initialElfCount;
        int elfStrength = 4;
        while (true){
            try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
                in = readInput(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            in.setElfStrength(elfStrength);
            long l = in.battleUntilItIsOverOrAnElfDies();
            if(in.countElfs() == elfCount){
                return l;
            }
            elfStrength++;
        }
    }


    public CaveBattle readInput(Stream<String> stream) {
        return new CaveBattle(stream
                .collect(Collectors.toList()));
    }

    public Day15() {
        System.out.println("::: Starting Day 15 :::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            in = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //long part1 = getPart1();
        //System.out.println(": answer to part 1 :");
        //System.out.println(part1);
        long part2 = getPart2(inputPath);
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
