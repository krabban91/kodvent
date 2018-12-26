package krabban91.kodvent.kodvent.y2018.d01;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class FrequencyHandler {

    private List<Integer> frequencyDeltas;
    private List<Integer> frequencies = new LinkedList<>();
    private Integer firstTwice = null;

    public int getPart1() {
        return frequencyDeltas.stream().reduce(0, Integer::sum);
    }

    public int getPart2() {
        int startValue = 0;
        while (firstTwice == null) {
            startValue = frequencyDeltas.stream().reduce(startValue, this::sum);
        }
        return firstTwice;
    }

    private void loadInput(Stream<String> stream) {
        frequencyDeltas = stream
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private int sum(int l, int r) {
        int frequency = l + r;
        if (frequencies.contains(frequency)) {
            if (firstTwice == null) {
                firstTwice = frequency;
            }
        } else {
            frequencies.add(frequency);
        }
        return frequency;
    }

    public FrequencyHandler() {
        System.out.println("::: Starting Day 1 :::");
        String inputPath = "y2018/d01/input.txt";
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            loadInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(": answer to part 1 :");
        int part1 = getPart1();
        System.out.println(part1);
        System.out.println(": answer to part 2 :");
        int part2 = getPart2();
        System.out.println(part2);
    }
}
