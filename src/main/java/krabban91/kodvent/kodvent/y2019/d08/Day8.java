package krabban91.kodvent.kodvent.y2019.d08;

import krabban91.kodvent.kodvent.utilities.Input;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Day8 {
    List<Integer> in;

    public Day8() {
        System.out.println("::: Starting Day 8 :::");
        String inputPath = "y2019/d08/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {

        Map<Integer, Map<Point, Integer>> image = getImage();
        Optional<Map<Point, Integer>> max = image.values().stream()
                .min(Comparator.comparingLong(m -> m.values().stream().filter(i -> i == 0).count()));
        return max
                .map(m-> m.values().stream().filter(i-> i ==1).count() * m.values().stream().filter(i-> i ==2).count())
                .orElse(-1L);
        //2378 too high

    }

    public Map<Integer, Map<Point, Integer>> getImage() {
        int width = 25;
        int height = 6;
        Map<Integer, Map<Point, Integer>> image = new HashMap<>();
        for (int i = 0; i < in.size(); i++) {
            image.computeIfAbsent(i / (width * height), k -> new HashMap<>());
            image.get(i / (width * height)).put(new Point((i%(width*height) )/ width, i % width), in.get(i));
        }
        return image;
    }

    public long getPart2() {
        int width = 25;
        int height = 6;
        Map<Point, Integer> finalImage = new HashMap<>();
        Map<Integer, Map<Point, Integer>> image = getImage();
        for (int i = 0; i < in.size(); i++) {

            Integer integer = image.get(i / (width * height)).get(new Point((i % (width * height)) / width, i % width));
            if(integer != 2){
                finalImage.computeIfAbsent(new Point((i%(width*height) )/ width, i % width), k-> integer);
            }
        }
        StringBuilder b = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Integer obj = finalImage.get(new Point(y, x));
                b.append(obj == 0 ? " ":"*");
            }
            b.append("\n");
        }
        System.out.println(b);
        return -1;
    }

    public void readInput(String inputPath) {
        in = Input.getSingleLine(inputPath).chars().mapToObj(i -> (char)i + "")
                .map(Integer::parseInt).collect(Collectors.toList());
    }
}
