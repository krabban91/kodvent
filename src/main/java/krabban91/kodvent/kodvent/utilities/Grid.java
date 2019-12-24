package krabban91.kodvent.kodvent.utilities;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid<V> {

    private List<? extends List<V>> raw;

    public Grid(){

    }

    public Grid(List<? extends List<V>> grid){
        this.raw = grid;
    }

    public Set<Point> indicesMatching(Predicate<V> filter){
        return IntStream.range(0, raw.size()).mapToObj(y ->
                IntStream.range(0, raw.get(y).size())
                        .filter(x -> filter.test(raw.get(y).get(x)))
                        .mapToObj(x -> new Point(x, y))
                        .collect(Collectors.toSet()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public List<V> getSurroundingTiles(int row, int col, List<? extends List<V>> item) {
        return IntStream
                .rangeClosed(Math.max(row - 1, 0), Math.min(row + 1, item.size() - 1))
                .mapToObj(i -> IntStream
                        .rangeClosed(Math.max(col - 1, 0), Math.min(col + 1, item.get(row).size() - 1))
                        .mapToObj(j -> (i == row && j == col) ? null : item.get(i).get(j))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    public List<V> getSurroundingTiles(int row, int col) {
        return IntStream
                .rangeClosed(Math.max(row - 1, 0), Math.min(row + 1, this.raw.size() - 1))
                .mapToObj(i -> IntStream
                        .rangeClosed(Math.max(col - 1, 0), Math.min(col + 1, this.raw.get(row).size() - 1))
                        .mapToObj(j -> (i == row && j == col) ? null : this.raw.get(i).get(j))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    public List<V> getAdjacentTiles(int row, int col) {
        List<V> collect = IntStream
                .rangeClosed(Math.max(row - 1, 0), Math.min(row + 1, this.raw.size() - 1))
                .mapToObj(i -> IntStream
                        .rangeClosed(Math.max(col - 1, 0), Math.min(col + 1, this.raw.get(row).size() - 1))
                        .mapToObj(j -> (i == row && j == col) ? null : (i == row || j == col) ? this.raw.get(i).get(j) : null)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return collect;
    }

    public List<Map.Entry<Point,V>> getSurroundingTilesWithPoints(int row, int col, boolean includeCenter) {
        return IntStream
                .rangeClosed(Math.max(row - 1, 0), Math.min(row + 1, this.raw.size() - 1))
                .mapToObj(i -> IntStream
                        .rangeClosed(Math.max(col - 1, 0), Math.min(col + 1, this.raw.get(row).size() - 1))
                        .mapToObj(j -> (!includeCenter && (i == row && j == col)) ? null : Map.entry(new Point(j,i),this.raw.get(i).get(j)))
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

    public void forEachRanged(List<? extends List<V>> grid, BiConsumer<Integer, Integer> action) {
        IntStream.range(0, grid.size())
                .forEach(row -> IntStream.range(0, grid.get(0).size())
                        .forEach(col -> action.accept(col, row)));
    }

    public void forEachRanged(BiConsumer<Integer, Integer> action) {
        IntStream.range(0, raw.size())
                .forEach(row -> IntStream.range(0, raw.get(0).size())
                        .forEach(col -> action.accept(col, row)));
    }

    public void forEach(Consumer<V> action) {
        this.raw.forEach(l -> l.forEach(action));
    }

    public V get(int x, int y){
        return this.raw.get(y).get(x);
    }

    public long sum(ToLongFunction<V> valueMethod){
        return this.raw.stream().mapToLong(l-> l.stream().mapToLong(valueMethod).sum()).sum();
    }

    public Grid<V> clone(Function<V, V> cloneMethod){
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
