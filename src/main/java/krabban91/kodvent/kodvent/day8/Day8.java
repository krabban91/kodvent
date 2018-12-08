package krabban91.kodvent.kodvent.day8;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day8 {
    private static String inputPath = "day8.txt";
    LicenseTreeNode licenseTree;

    public int getPart1() {
        return licenseTree.getCheckSum();
    }

    public int getPart2() {
        return licenseTree.getMetaDataSum();
    }

    public LicenseTreeNode readInput(Stream<String> stream) {
        return stream.map(this::parseLicense).findFirst().get();
    }

    public LicenseTreeNode parseLicense(String row) {
        List<String> strings = Arrays.asList(row.split(" "));
        return new LicenseTreeNode(
                new InputContainer(strings
                        .stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())));
    }

    public Day8() {
        System.out.println("::: Starting Day 7 :::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            licenseTree = readInput(stream);
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
