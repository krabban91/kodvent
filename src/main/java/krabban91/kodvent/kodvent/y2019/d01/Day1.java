package krabban91.kodvent.kodvent.y2019.d01;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.List;
import java.util.stream.Collectors;

public class Day1 {
    private List<Long> in;

    public Day1() {
        System.out.println("::: Starting Day 1 :::");
        String inputPath = "y2019/d01/input.txt";
        readInput(inputPath);
        long part1 = getPart1(in);
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2(in);
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1(List<Long> in) {
        return sumOfWeights(fuelRequirements(in));
    }

    public long getPart2(List<Long> in) {
        long neededFuel = 0;
        List<Long> masses = in;
        while (masses.size() > 0) {
            masses = fuelRequirements(masses);
            neededFuel += sumOfWeights(masses);
        }
        return neededFuel;
    }

    private List<Long> fuelRequirements(List<Long> masses) {
        return masses.stream()
                .map(l -> l / 3 - 2)
                .filter(l -> l > 0)
                .collect(Collectors.toList());
    }

    private long sumOfWeights(List<Long> masses) {
        return masses.stream()
                .mapToLong(l -> l)
                .sum();
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath)
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
