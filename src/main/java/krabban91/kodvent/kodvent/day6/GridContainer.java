package krabban91.kodvent.kodvent.day6;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GridContainer {
    private HashMap<Point, Integer>[][] grid;
    private int dx;
    private int dy;
    private HashMap<Point, Integer>[][] distanceMapping;

    public GridContainer(List<Point> cornerPoints, int extraPadding) {
        int minX = cornerPoints.stream().min(Comparator.comparingInt(p -> p.x)).get().x;
        int maxX = cornerPoints.stream().max(Comparator.comparingInt(p -> p.x)).get().x;
        int minY = cornerPoints.stream().min(Comparator.comparingInt(p -> p.y)).get().y;
        int maxY = cornerPoints.stream().max(Comparator.comparingInt(p -> p.y)).get().y;
        dx = minX;
        dy = minY;
        grid = new HashMap[maxX - minX + 1][maxY - minY + 1];
        distanceMapping = new HashMap[maxX - minX + 1][maxY - minY + 1];
    }

    public int getWidth() {
        return grid.length;
    }

    public int getHeight() {
        return grid.length == 0 ? 0 : grid[0].length;
    }

    public void assignAvailablePatches(Point point) {
        IntStream.range(0, getWidth()).forEach(x -> IntStream.range(0, getHeight()).forEach(y -> {
            if (grid[x][y] == null) {
                grid[x][y] = new HashMap<>();
            }
            HashMap<Point, Integer> patch = grid[x][y];
            int distance = CoordinatePicker.manhattanDistance(point, new Point(x + dx, y + dy));
            if (!patch.entrySet().stream().anyMatch(e -> e.getValue() < distance)) {
                if (patch.entrySet().stream().anyMatch(e -> e.getValue() > distance)) {
                    patch.clear();
                }
                patch.put(point, distance);
            }
        }));
    }

    public void mapDistances(Point point) {
        IntStream.range(0, getWidth()).forEach(x -> IntStream.range(0, getHeight()).forEach(y -> {
            if (distanceMapping[x][y] == null) {
                distanceMapping[x][y] = new HashMap<>();
            }
            distanceMapping[x][y].put(point, CoordinatePicker.manhattanDistance(point, new Point(x + dx, y + dy)));
        }));
    }

    public int getAreaOfNearRegion(int distance) {
        return Stream.of(distanceMapping)
                .map(col -> Stream.of(col)
                        .map(CoordinatePicker::sumOfDistances)
                        .filter(val -> val < distance)
                        .reduce(0, (l, r) -> l + 1))
                .reduce(0, Integer::sum)
                .intValue();
    }

    public int getManhattanArea(Point point) {
        return Stream.of(grid)
                .map(col -> Stream.of(col)
                        .filter(m -> m.size() == 1)
                        .filter(m -> m.entrySet().stream()
                                .anyMatch(e -> e.getKey().equals(point)))
                        .count())
                .reduce(0l, Long::sum)
                .intValue();
    }
}
