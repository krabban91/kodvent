package krabban91.kodvent.kodvent.utilities;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid<V> {

    private final List<? extends List<V>> raw;

    public Grid(List<? extends List<V>> grid) {
        this.raw = grid;
    }

    public int height() {
        return this.raw.size();
    }

    public int width() {
        return this.raw.get(0).size();
    }

    public Set<Point> indicesMatching(Predicate<V> filter) {
        return IntStream.range(0, raw.size()).mapToObj(y ->
                IntStream.range(0, raw.get(y).size())
                        .filter(x -> filter.test(raw.get(y).get(x)))
                        .mapToObj(x -> new Point(x, y))
                        .collect(Collectors.toSet()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public List<V> getSurroundingTiles(Point p) {
        return getNearestTilesOfSurroundingDirections(p, v -> true);
    }

    public List<V> getNearestTilesOfSurroundingDirections(Point from, Predicate<V> predicate) {
        return getNearestTilesOfSurroundingDirections(from.x, from.y, predicate);
    }

    public List<V> getNearestTilesOfSurroundingDirections(int x, int y, Predicate<V> predicate) {
        List<Point> directions = List.of(new Point(0, -1), new Point(-1, -1), new Point(-1, 0), new Point(-1, 1), new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(1, -1));
        return directions.stream()
                .map(p -> getNearestInDirection(x, y, p, predicate))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public List<V> getNearestTilesOfAdjacentDirections(Point from, Predicate<V> predicate) {
        return getNearestTilesOfAdjacentDirections(from.x, from.y, predicate);
    }
    public List<V> getNearestTilesOfAdjacentDirections(int x, int y, Predicate<V> predicate) {
        List<Point> directions = List.of(new Point(0, -1), new Point(-1, 0), new Point(0, 1), new Point(1, 0));
        return directions.stream()
                .map(p -> getNearestInDirection(x, y, p, predicate))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<V> getNearestInDirection(int x, int y, Point direction, Predicate<V> predicate) {
        int dx = x + direction.x;
        int dy = y + direction.y;
        Optional<V> tile = this.get(dx, dy);
        while (tile.isPresent() && !predicate.test(tile.get())) {
            dx += direction.x;
            dy += direction.y;
            tile = this.get(dx, dy);
        }
        return tile;
    }

    public List<V> getAdjacentTiles(Point p) {
        return getNearestTilesOfAdjacentDirections(p, v -> true);
    }

    public List<Map.Entry<Point, V>> getSurroundingTilesWithPoints(Point p, boolean includeCenter) {
        return getSurroundingTilesWithPoints(p.x, p.y, includeCenter);
    }

    public List<Map.Entry<Point, V>> getSurroundingTilesWithPoints(int x, int y, boolean includeCenter) {
        return IntStream
                .rangeClosed(Math.max(y - 1, 0), Math.min(y + 1, this.raw.size() - 1))
                .mapToObj(i -> IntStream
                        .rangeClosed(Math.max(x - 1, 0), Math.min(x + 1, this.raw.get(y).size() - 1))
                        .mapToObj(j -> (!includeCenter && (i == y && j == x)) ? null : Map.entry(new Point(j, i), this.raw.get(i).get(j)))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void forEachRangeClosed(int x0, int x1, int y0, int y1, BiConsumer<Integer, Integer> action) {
        IntStream.rangeClosed(y0, y1)
                .forEach(row -> IntStream.rangeClosed(x0, x1)
                        .forEach(col -> action.accept(col, row)));
    }

    public void forEachRanged(BiConsumer<Integer, Integer> action) {
        IntStream.range(0, raw.size())
                .forEach(row -> IntStream.range(0, raw.get(0).size())
                        .forEach(col -> action.accept(col, row)));
    }

    public Grid<V> map(BiFunction<V, Point, V> function) {
        return new Grid<>(IntStream.range(0, raw.size())
                .mapToObj(y -> IntStream.range(0, raw.get(0).size())
                        .mapToObj(x -> function.apply(this.raw.get(y).get(x), new Point(x, y)))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    public void forEach(Consumer<V> action) {
        this.raw.forEach(l -> l.forEach(action));
    }

    public void forEach(BiConsumer<V, Point> action) {
        IntStream.range(0, raw.size()).forEach(y -> IntStream.range(0, raw.get(0).size()).forEach(x -> action.accept(this.raw.get(y).get(x), new Point(x, y))));
    }

    public Optional<V> get(Point p) {
        return this.get(p.x, p.y);
    }

    public Optional<V> get(int x, int y) {
        return (x < 0 || x >= width() || y < 0 || y >= height()) ?
                Optional.empty() :
                Optional.of(this.raw.get(y).get(x));
    }

    public long sum(ToLongFunction<V> valueMethod) {
        return this.raw.stream().mapToLong(l -> l.stream().mapToLong(valueMethod).sum()).sum();
    }

    public Grid<V> clone(Function<V, V> cloneMethod) {
        return new Grid<>(raw.stream()
                .map(l -> l.stream()
                        .map(cloneMethod)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    public List<? extends List<V>> getRaw() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grid<?> grid = (Grid<?>) o;

        return raw.equals(grid.raw);
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

}
