package krabban91.kodvent.kodvent.y2018.d06;

import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoordinatePicker {

    private List<Point> locations;
    private GridContainer grid;

    public int getPart1() {
        return locations.stream()
                .filter(this::hasFiniteArea)
                .map(grid::getManhattanArea)
                .max(Comparator.comparingInt(e -> e.intValue())).get();
    }

    public int getPart2(int distanceLimit) {
        return grid.getAreaOfNearRegion(distanceLimit);
    }

    public GridContainer setUpGrid() {
        GridContainer gridContainer = new GridContainer(getCornerCoordinates());
        gridContainer.measureDistancesToCoordinates(locations);
        return gridContainer;
    }

    public static int manhattanDistance(Point point, Point other) {
        return Math.abs(point.x - other.x) + Math.abs(point.y - other.y);
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

    public static boolean isCloserToThan(Point point, Point other, int distance) {
        int distance1 = manhattanDistance(point, other);
        return distance1 <= distance;
    }

    private List<Point> getCornerCoordinates() {
        return locations.stream()
                .filter(this::hasInfinateArea)
                .collect(Collectors.toList());
    }

    private boolean isAnyCloserThan(Point point, Point other, int distance) {
        return locations.stream()
                .filter(p -> !p.equals(point))
                .anyMatch(p -> isCloserToThan(p, other, distance));
    }

    public Point mapToPoint(String row) {
        String[] strings = row.split(", ");
        int x = Integer.parseInt(strings[0]);
        int y = Integer.parseInt(strings[1]);
        return new Point(x, y);
    }

    public List<Point> readInput(Stream<String> stream) {
        return stream.map(this::mapToPoint).collect(Collectors.toList());
    }

    public CoordinatePicker() {
        System.out.println("::: Starting Day 6 :::");
        String inputPath = "y2018/d06/input.txt";
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            locations = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        grid = this.setUpGrid();
        int part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2(10000);
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
