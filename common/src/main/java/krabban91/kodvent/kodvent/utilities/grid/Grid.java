package krabban91.kodvent.kodvent.utilities.grid;

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
        List<Integer> xs = List.of(-1, 0, 1);
        List<Point> ys = xs.stream().map(v -> List.of(new Point(v, -1), new Point(v, 0), new Point(v, 1))).flatMap(List::stream).collect(Collectors.toList());
        return ys.stream().filter(p -> !p.equals(new Point(0, 0)))
                .map(p -> getNearestInDirection(from, p, predicate))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public List<V> getNearestTilesOfAdjacentDirections(Point from, Predicate<V> predicate) {
        List<Point> directions = List.of(new Point(0, -1), new Point(-1, 0), new Point(0, 1), new Point(1, 0));
        return directions.stream()
                .map(p -> getNearestInDirection(from, p, predicate))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<V> getNearestInDirection(Point p, Point direction, Predicate<V> predicate) {
        int dx = p.x + direction.x;
        int dy = p.y + direction.y;
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

    public Optional<Point> minX(BiPredicate<V, Point> predicate) {
        return IntStream.range(0, height())
                .mapToObj(y -> IntStream
                        .range(0, width())
                        .mapToObj(x -> new Point(x, y))
                        .filter(p -> this.get(p).map(v -> predicate.test(v, p)).orElse(false))
                        .min(Comparator.comparingInt(v -> v.x))
                ).flatMap(Optional::stream).min(Comparator.comparingInt(v -> v.x));
    }

    public Optional<Point> minY(BiPredicate<V, Point> predicate) {
        return IntStream.range(0, height())
                .mapToObj(y -> IntStream
                        .range(0, width())
                        .mapToObj(x -> new Point(x, y))
                        .filter(p -> this.get(p).map(v -> predicate.test(v, p)).orElse(false))
                        .min(Comparator.comparingInt(v -> v.y))
                ).flatMap(Optional::stream).min(Comparator.comparingInt(v -> v.y));
    }

    public Optional<Point> maxX(BiPredicate<V, Point> predicate) {
        return IntStream.range(0, height())
                .mapToObj(y -> IntStream
                        .range(0, width())
                        .mapToObj(x -> new Point(x, y))
                        .filter(p -> this.get(p).map(v -> predicate.test(v, p)).orElse(false))
                        .max(Comparator.comparingInt(v -> v.x))
                ).flatMap(Optional::stream).max(Comparator.comparingInt(v -> v.x));
    }

    public Optional<Point> maxY(BiPredicate<V, Point> predicate) {
        return IntStream.range(0, height())
                .mapToObj(y -> IntStream
                        .range(0, width())
                        .mapToObj(x -> new Point(x, y))
                        .filter(p -> this.get(p).map(v -> predicate.test(v, p)).orElse(false))
                        .max(Comparator.comparingInt(v -> v.y))
                ).flatMap(Optional::stream).max(Comparator.comparingInt(v -> v.y));
    }

    public Grid<V> expand(Function<Point, V> generator, int steps) {
        return new Grid<>(IntStream.range(-steps, raw.size() + steps)
                .mapToObj(y -> IntStream.range(-steps, raw.get(0).size() + steps)
                        .mapToObj(x -> this.get(x, y).orElseGet(() -> generator.apply(new Point(x, y))))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    public Grid<V> shrink(BiPredicate<V, Point> predicate) {
        Integer minX = minX(predicate).map(p -> p.x).orElse(0);
        Integer minY = minY(predicate).map(p -> p.y).orElse(0);
        Integer maxX = maxX(predicate).map(p -> p.x).orElse(0);
        Integer maxY = maxY(predicate).map(p -> p.y).orElse(0);
        List<List<V>> collect = this.getRaw().subList(minY, maxY + 1).stream().map(l -> l.subList(minX, maxX + 1)).collect(Collectors.toList());
        return new Grid<>(collect);
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
