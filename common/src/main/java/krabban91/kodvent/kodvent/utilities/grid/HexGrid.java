package krabban91.kodvent.kodvent.utilities.grid;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HexGrid<V> extends Grid<V> {
    public static Map<String, Point> directions = Collections.unmodifiableMap(Map.of(
            "se", new Point(0, 1),
            "e", new Point(1, 0),
            "ne", new Point(1, -1),
            "nw", new Point(0, -1),
            "w", new Point(-1, 0),
            "sw", new Point(-1, 1)));

    public static Point move(String direction, Point from) {
        Point delta = HexGrid.directions.get(direction);
        return new Point(from.x + delta.x, from.y + delta.y);
    }

    public HexGrid(List<? extends List<V>> grid) {
        super(grid);
    }

    @Override
    public List<V> getSurroundingTiles(Point p) {
        return directions.values().stream()
                .map(d -> new Point(p.x + d.x, p.y + d.y))
                .map(this::get)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Override
    public HexGrid<V> expand(Function<Point, V> generator, int steps) {
        return new HexGrid<>(super.expand(generator, steps).getRaw());
    }

    @Override
    public HexGrid<V> shrink(BiPredicate<V, Point> predicate) {
        return new HexGrid<>(super.shrink(predicate).getRaw());
    }

    @Override
    public HexGrid<V> map(BiFunction<V, Point, V> function) {
        return new HexGrid<>(super.map(function).getRaw());
    }


        @Override
    public List<V> getNearestTilesOfSurroundingDirections(Point from, Predicate<V> predicate) {
        throw new UnsupportedOperationException("Not supported for HexGrids");
    }

    @Override
    public List<V> getNearestTilesOfAdjacentDirections(Point from, Predicate<V> predicate) {
        throw new UnsupportedOperationException("Not supported for HexGrids");
    }

    @Override
    public List<V> getAdjacentTiles(Point p) {
        throw new UnsupportedOperationException("Not supported for HexGrids");
    }

    @Override
    public List<Map.Entry<Point, V>> getSurroundingTilesWithPoints(Point p, boolean includeCenter) {
        throw new UnsupportedOperationException("Not supported for HexGrids");
    }

    @Override
    public List<Map.Entry<Point, V>> getSurroundingTilesWithPoints(int x, int y, boolean includeCenter) {
        throw new UnsupportedOperationException("Not supported for HexGrids");
    }
}
