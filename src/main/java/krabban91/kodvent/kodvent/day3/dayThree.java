package krabban91.kodvent.kodvent.day3;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class dayThree {

    private static String inputPath = "day3.txt";
    public dayThree() {

        System.out.println("::: Starting Day 3:::");
        long part1 = 0;
        part1 = getPart1();
        System.out.println("::: answer to part 1:::");
        System.out.println(part1);
        String part2 = getPart2();
        System.out.println("::: answer to part 2:::");
        System.out.println(part2);
    }

    private String getPart2() {
        return "<no answer>";

    }

    private long getPart1() {
        return 0;
    }
}
