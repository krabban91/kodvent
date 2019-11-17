package krabban91.kodvent.kodvent.utilities;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid<V> {

    private List<? extends List<V>> grid;

    public Grid(){

    }

    public Grid(List<? extends List<V>> grid){
        this.grid = grid;
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

    public V get(int x, int y){
        return this.grid.get(y).get(x);
    }

    public long sum(ToLongFunction<V> valueMethod){
        return this.grid.stream().mapToLong(l-> l.stream().mapToLong(valueMethod).sum()).sum();
    }
}
