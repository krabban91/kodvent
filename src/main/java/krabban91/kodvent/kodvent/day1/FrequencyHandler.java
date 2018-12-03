package krabban91.kodvent.kodvent.day1;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class FrequencyHandler {

    private static String inputPath = "day1.txt";

    private List<Integer> frequencies = new LinkedList<>();
    private Integer firstTwice = null;

    public  FrequencyHandler() {
        System.out.println("::: Starting Day 1 :::");
        int part1 = 0;
        part1 = getPart1(part1);
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        while (firstTwice == null) {
            part1 = getPart1(part1);
        }
        System.out.println(": answer to part 2 :");
        Integer part2 = firstTwice;
        System.out.println(part2);
    }

    private int getPart1(int part1) {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            part1 = stream.mapToInt(Integer::parseInt).reduce(part1,this::sum);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return part1;
    }

    private int sum(int l, int r){
        int frequency = l + r;
        if(frequencies.contains(frequency)){
            if(firstTwice == null){
                firstTwice = frequency;
            }
        }
        else {
            frequencies.add(frequency);
        }
        return frequency;
    }
}
