package krabban91.kodvent.kodvent.y2018.d06;

import krabban91.kodvent.kodvent.utilities.Distances;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GridContainer {
    private HashMap<Point, Integer>[][] grid;
    private HashMap<Point, Integer>[][] gridAll;
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
        gridAll = new HashMap[maxX - minX + 1][maxY - minY + 1];
    }

    public void measureDistancesToCoordinates(List<Point> coordinates) {
        coordinates.stream().forEach(this::assignAvailablePatches);
    }

    public void assignAvailablePatches(Point point) {
        IntStream.range(0, getWidth()).forEach(x -> IntStream.range(0, getHeight()).forEach(y -> {
            if (grid[x][y] == null) {
                grid[x][y] = new HashMap<>();
                gridAll[x][y] = new HashMap<>();
            }
            Point thisPoint = new Point(x + dx, y + dy);
            int distance = Distances.manhattan(point, thisPoint);
            HashMap<Point, Integer> patch = grid[x][y];
            if (!patch.entrySet().stream().anyMatch(e -> e.getValue() < distance)) {
                if (patch.entrySet().stream().anyMatch(e -> e.getValue() > distance)) {
                    patch.clear();
                }
                patch.put(point, distance);
            }
            gridAll[x][y].put(point, distance);
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
                        .filter(m -> m.size() == 1)
                        .count())
                .reduce(0l, Long::sum)
                .intValue();
    }

    public int getAreaOfNearRegion(int limit) {
        return Stream.of(gridAll)
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
