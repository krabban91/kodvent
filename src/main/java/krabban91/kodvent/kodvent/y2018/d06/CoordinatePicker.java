package krabban91.kodvent.kodvent.y2018.d06;

import krabban91.kodvent.kodvent.utilities.Distances;
import krabban91.kodvent.kodvent.utilities.Input;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CoordinatePicker {

    private List<Point> locations;
    private GridContainer grid;

    public int getPart1() {
        return locations.stream()
                .filter(this::hasFiniteArea)
                .map(grid::getManhattanArea)
                .max(Comparator.comparingInt(e -> e)).orElse(-1);
    }

    public int getPart2(int distanceLimit) {
        return grid.getAreaOfNearRegion(distanceLimit);
    }

    public GridContainer setUpGrid() {
        GridContainer gridContainer = new GridContainer(getCornerCoordinates());
        gridContainer.measureDistancesToCoordinates(locations);
        return gridContainer;
    }

    public boolean hasFiniteArea(Point point) {
        return !hasInfinateArea(point);
    }

    public boolean hasInfinateArea(Point point) {

        Point topLeft = new Point(-200, -200);
        Point topRight = new Point(20000, -200);
        Point bottomLeft = new Point(-200, 20000);
        Point bottomRight = new Point(20000, 20000);

        return !(isAnyCloserThan(point, topLeft, Distances.manhattan(point, topLeft)) &&
                isAnyCloserThan(point, topRight, Distances.manhattan(point, topRight)) &&
                isAnyCloserThan(point, bottomLeft, Distances.manhattan(point, bottomLeft)) &&
                isAnyCloserThan(point, bottomRight, Distances.manhattan(point, bottomRight)));
    }

    public static boolean isCloserToThan(Point point, Point other, int distance) {
        int distance1 = Distances.manhattan(point, other);
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

    public void readInput(String path) {
        this.locations = Input.getLines(path).stream().map(this::mapToPoint).collect(Collectors.toList());
    }


    public CoordinatePicker() {
        System.out.println("::: Starting Day 6 :::");
        String inputPath = "y2018/d06/input.txt";
        readInput(inputPath);
        grid = this.setUpGrid();
        int part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2(10000);
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public List<Point> getLocations() {
        return locations;
    }
}
