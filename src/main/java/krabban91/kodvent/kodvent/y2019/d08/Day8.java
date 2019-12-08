package krabban91.kodvent.kodvent.y2019.d08;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        String part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        Map<Integer, Map<Point, Integer>> image = getImage(25, 6);
        return image.values()
                .stream()
                .min(Comparator.comparingLong(m -> m.values()
                        .stream()
                        .filter(i -> i == 0)
                        .count()))
                .map(m -> m.values().stream().filter(i -> i == 1).count() * m.values().stream().filter(i -> i == 2).count())
                .orElse(-1L);
    }

    public String getPart2() {
        int width = 25;
        int height = 6;
        Map<Point, Integer> finalImage = new HashMap<>();

        Map<Integer, Map<Point, Integer>> image = getImage(25, 6);
        for (int i = 0; i < in.size(); i++) {
            Integer integer = image.get(layerNumber(width, height, i)).get(new Point(getX(width, height, i), getY(width, i)));
            if (integer != 2) {
                finalImage.putIfAbsent(new Point(getY(width, i), getX(width, height, i)), integer);
            }
        }
        return LogUtils.mapToText(finalImage, i -> i == 0 ? " " : "*");
    }

    public void readInput(String inputPath) {
        in = Input.getSingleLine(inputPath).chars().mapToObj(i -> (char) i + "")
                .map(Integer::parseInt).collect(Collectors.toList());
    }

    private Map<Integer, Map<Point, Integer>> getImage(int width, int height) {
        Map<Integer, Map<Point, Integer>> image = new HashMap<>();
        for (int i = 0; i < in.size(); i++) {
            image.putIfAbsent(layerNumber(width, height, i), new HashMap<>());
            image.get(layerNumber(width, height, i)).put(new Point(getX(width, height, i), getY(width, i)), in.get(i));
        }
        return image;
    }

    private int getY(int width, int i) {
        return i % width;
    }

    private int getX(int width, int height, int i) {
        return (getY(width * height, i)) / width;
    }

    private int layerNumber(int width, int height, int i) {
        return i / (width * height);
    }
}
