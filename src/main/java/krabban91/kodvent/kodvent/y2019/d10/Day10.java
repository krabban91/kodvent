package krabban91.kodvent.kodvent.y2019.d10;

import krabban91.kodvent.kodvent.utilities.Distances;
import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day10 {
    List<Point> in;

    public Day10() {
        System.out.println("::: Starting Day 10 :::");
        String inputPath = "y2019/d10/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        return getStation(new ArrayList<>(in))
                .map(e -> e.getValue().size())
                .orElse(-1);
    }

    public long getPart2() {
        List<Point> asteroidsList = new ArrayList<>(in);
        Point origo = getStation(asteroidsList)
                .get()
                .getKey();
        LinkedList<Point> blownUp = new LinkedList<>();
        double currentAngle = Math.PI / 2;
        while (blownUp.size() != 200) {
            Map<Double, Set<Point>> potentialTargets = getPotentialTargets(origo, asteroidsList);
            Map.Entry<Double, Point> target = target(origo, potentialTargets, strictlyPositiveRadians(currentAngle));
            currentAngle = target.getKey();
            blownUp.addLast(target.getValue());
            asteroidsList.remove(target.getValue());
        }
        return blownUp.getLast().x * 100 + blownUp.getLast().y;
    }

    private Optional<Map.Entry<Point, Set<Double>>> getStation(List<Point> asteroidsList) {
        Map<Point, Set<Double>> collect1 = asteroidsList.stream()
                .collect(Collectors.toMap(
                        p -> p,
                        origo -> asteroidsList.stream()
                                .map(asteroid -> satan2(origo, asteroid))
                                .collect(Collectors.toSet())));
        return collect1.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()));
    }


    private Map<Double, Set<Point>> getPotentialTargets(Point origo, List<Point> asteroidsList) {
        Map<Double, Set<Point>> map = new HashMap<>();
        asteroidsList
                .forEach(e -> {
                    double angle = satan2(origo, e);
                    map.computeIfAbsent(angle, a -> new HashSet<>());
                    map.get(angle).add(e);
                });
        return map;
    }

    private Map.Entry<Double, Point> target(Point origo, Map<Double, Set<Point>> potential, double currentAngle) {
        return potential.entrySet().stream()
                .filter(e -> e.getKey().compareTo(currentAngle) < 0)
                .max(Comparator.comparingDouble(Map.Entry::getKey))
                .map(e -> Map.entry(
                        e.getKey(),
                        e.getValue()
                                .stream()
                                .min(Comparator.comparingInt(p -> Distances.manhattan(origo, p)))
                                .get()))
                .get();
    }


    private double satan2(Point origo, Point point) {
        // adjust vector to start from (0,0)
        // adjust y factor to negative axis
        double x = point.x - origo.x;
        double y = -1 * (point.y - origo.y);
        double angle = Math.atan2(y, x);
        // ensure that angle is always positive
        return positiveRadians(angle);
    }

    private double positiveRadians(double angle) {
        double fullCycle = Math.PI * 2;
        return (angle + fullCycle) % (fullCycle);
    }

    private double strictlyPositiveRadians(double angle) {
        double out = positiveRadians(angle);
        return out == 0 ? Math.PI * 2 : out;
    }


    public void readInput(String inputPath) {
        List<String> lines = Input.getLines(inputPath);
        in = IntStream.range(0, lines.size())
                .mapToObj(y -> IntStream.range(0, lines.get(y).length())
                        .filter(x -> lines.get(y).charAt(x) == '#')
                        .mapToObj(x -> new Point(x, y))
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
