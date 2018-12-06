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

    public GridContainer(List<Point> cornerPoints) {
        int minX = cornerPoints.stream().min(Comparator.comparingInt(p -> p.x)).get().x;
        int maxX = cornerPoints.stream().max(Comparator.comparingInt(p -> p.x)).get().x;
        int minY = cornerPoints.stream().min(Comparator.comparingInt(p -> p.y)).get().y;
        int maxY = cornerPoints.stream().max(Comparator.comparingInt(p -> p.y)).get().y;
        dx = minX;
        dy = minY;
        grid = new HashMap[maxX - minX + 1][maxY - minY + 1];
    }

    public void measureDistancesToCoordinates(List<Point> coordinates) {
        coordinates.stream().forEach(this::assignAvailablePatches);
    }

    public void assignAvailablePatches(Point point) {
        IntStream.range(0, getWidth()).forEach(x -> IntStream.range(0, getHeight()).forEach(y -> {
            if (grid[x][y] == null) {
                grid[x][y] = new HashMap<>();
            }
            grid[x][y].put(point, CoordinatePicker.manhattanDistance(point, new Point(x + dx, y + dy)));
        }));
    }

    public int getWidth() {
        return grid.length;
    }

    public int getHeight() {
        return grid.length == 0 ? 0 : grid[0].length;
    }

    public int getManhattanArea(Point point) {
        return Stream.of(grid)
                .map(col -> Stream.of(col)
                        .filter(m -> m.containsKey(point))
                        .filter(m -> m.entrySet()
                                .stream()
                                .min(Comparator.comparingInt(d -> d.getValue()))
                                .get().getKey().equals(point))
                        .count())
                .reduce(0l, Long::sum)
                .intValue();
    }

    public int getAreaOfNearRegion(int limit) {
        return Stream.of(grid)
                .map(col -> Stream.of(col)
                        .map(GridContainer::sumOfDistances)
                        .filter(val -> val < limit)
                        .reduce(0, (l, r) -> l + 1))
                .reduce(0, Integer::sum)
                .intValue();
    }

    public static int sumOfDistances(HashMap<Point, Integer> map) {
        return map.entrySet().stream()
                .map(e -> e.getValue())
                .reduce(0, Integer::sum);
    }
}
