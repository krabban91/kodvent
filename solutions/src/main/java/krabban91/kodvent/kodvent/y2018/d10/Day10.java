package krabban91.kodvent.kodvent.y2018.d10;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 {
    List<Star> stars;
    int second = 0;

    public String getPart1() {
        while (!shouldVisualize()) {
            waitSecond();
        }
        System.out.println("Star sky second " + second);
        visualizeStars();
        return "HI";
    }

    private boolean shouldVisualize() {
        return stars.stream()
                .allMatch(e -> stars.stream()
                        .anyMatch(f -> !f.equals(e) && f.isWithinManhattanDistanceOf(e, 2)));
    }

    public long getPart2() {
        return second;
    }

    private void waitSecond() {
        second++;
        stars.forEach(Star::moveOneSecond);
    }

    private void visualizeStars() {
        int minX = stars.stream().min(Comparator.comparingInt(Star::getX)).get().getX();
        int maxX = stars.stream().max(Comparator.comparingInt(Star::getX)).get().getX();
        Map<Integer, List<Star>> collect = stars.stream().collect(Collectors.groupingBy(Star::getY));
        collect.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).forEach(e -> visualizeRow(e, minX, maxX));
    }

    private void visualizeRow(Map.Entry<Integer, List<Star>> integerListEntry, int min, int max) {
        StringBuilder build = new StringBuilder();
        IntStream.range(min, max + 1).forEach(e -> this.visualizeStarIfItExists(e, integerListEntry, build));
        System.out.println(build.toString());
    }

    private void visualizeStarIfItExists(int e, Map.Entry<Integer, List<Star>> integerListEntry, StringBuilder build) {
        Optional<Star> any = integerListEntry.getValue().stream().filter(s -> s.hasXValue(e)).findAny();
        if (any.isPresent()) {
            build.append('#');
        } else {
            build.append('.');
        }
    }

    public void readInput(String path) {
        this.second = 0;
        this.stars = Input.getLines(path).stream().map(Star::new).collect(Collectors.toList());
    }

    public Day10() {
        System.out.println("::: Starting Day 10 :::");
        String inputPath = "y2018/d10/input.txt";
        readInput(inputPath);
        String part1 = getPart1();
        System.out.println(": answer to part 1 is printed above.");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
