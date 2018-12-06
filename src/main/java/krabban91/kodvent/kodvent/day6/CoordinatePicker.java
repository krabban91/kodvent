package krabban91.kodvent.kodvent.day6;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CoordinatePicker {
    private static String inputPath = "day6.txt";

    private List<Point> locations;

    public CoordinatePicker() {
        System.out.println("::: Starting Day 5:::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            readInput(stream);
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

    public int getPart1() {
        List<Point> cornerPoints = locations.stream()
                .filter(this::hasInfinateArea)
                .collect(Collectors.toList());
        GridContainer grid = setUpGrid(cornerPoints);
        locations.stream().forEach(grid::assignAvailablePatches);
        return grid.getManhattanArea(locations.stream()
                .filter(this::hasFiniteArea)
                .max(Comparator.comparingInt(grid::getManhattanArea))
                .get());
    }

    public GridContainer setUpGrid(List<Point> corners) {
        return new GridContainer(corners, 0);
    }

    public int getGridWidth(List<Point> corners) {
        return -1;
    }

    public int getGridHeight(List<Point> corners) {
        return -1;
    }

    public int getPart2() {
        return -1;
    }

    public boolean hasFiniteArea(Point point) {
        return !hasInfinateArea(point);
    }

    public boolean hasInfinateArea(Point point) {

        Point topLeft = new Point(-200, -200);
        Point topRight = new Point(20000, -200);
        Point bottomLeft = new Point(-200, 20000);
        Point bottomRight = new Point(20000, 20000);

        return !(isAnyCloserThan(point, topLeft, manhattanDistance(point, topLeft)) &&
                isAnyCloserThan(point, topRight, manhattanDistance(point, topRight)) &&
                isAnyCloserThan(point, bottomLeft, manhattanDistance(point, bottomLeft)) &&
                isAnyCloserThan(point, bottomRight, manhattanDistance(point, bottomRight)));
    }

    private boolean isAnyCloserThan(Point point, Point topLeft, int topLeftDistance) {
        List<Point> collect = locations.stream()
                .filter(p -> !p.equals(point)).collect(Collectors.toList());
        return collect.stream().anyMatch(p -> isCloserToThan(p, topLeft, topLeftDistance));
    }

    public static boolean isCloserToThan(Point point, Point other, int distance) {
        int distance1 = manhattanDistance(point, other);
        return distance1 <= distance;
    }

    public static int manhattanDistance(Point point, Point other) {
        return (int) (Math.abs(point.getX() - other.getX()) + Math.abs(point.getY() - other.getY()));
    }

    private void readInput(Stream<String> stream) {
        locations = stream.map(this::mapToPoint).collect(Collectors.toList());
    }

    public Point mapToPoint(String row) {
        String[] strings = row.split(", ");
        int x = Integer.parseInt(strings[0]);
        int y = Integer.parseInt(strings[1]);
        return new Point(x, y);
    }
}
