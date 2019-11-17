package krabban91.kodvent.kodvent.y2016.d03;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 {

    List<String> in;

    public Day3() {
        System.out.println("::: Starting Day 3 :::");
        String inputPath = "y2016/d03/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        List<List<Integer>> collect = getTriangleMeasurements();
        return collect.stream()
                .mapToInt(l -> isPossibleTriangle(l.get(0), l.get(1), l.get(2)) ? 1 : 0)
                .sum();
    }

    public long getPart2() {
        List<List<Integer>> lists = getFlippedTriangleMeasurements();
        return lists.stream()
                .mapToLong(l -> IntStream.range(0, l.size() / 3)
                        .mapToLong(i -> isPossibleTriangle(l.get(3 * i), l.get(3 * i + 1), l.get(3 * i + 2)) ? 1 : 0)
                        .sum())
                .sum();
    }

    private List<List<Integer>> getTriangleMeasurements() {
        return in.stream()
                .map(e -> Arrays.stream(e.split(" "))
                        .filter(l -> !l.isBlank())
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private List<List<Integer>> getFlippedTriangleMeasurements() {
        List<List<Integer>> lists = Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<List<Integer>> collect = getTriangleMeasurements();
        collect.stream()
                .forEachOrdered(l -> {
                    lists.get(0).add(l.get(0));
                    lists.get(1).add(l.get(1));
                    lists.get(2).add(l.get(2));
                });
        return lists;
    }

    public boolean isPossibleTriangle(int a, int b, int c) {
        return a + b > c && b + c > a && c + a > b;
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath);
    }
}
