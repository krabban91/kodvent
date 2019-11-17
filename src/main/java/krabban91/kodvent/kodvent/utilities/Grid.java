package krabban91.kodvent.kodvent.utilities;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid<V> {


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

    public void forEachRanged(int x0, int x1, int y0, int y1, BiConsumer<Integer, Integer> action) {
        IntStream.range(x0, x1)
                .forEach(row -> IntStream.range(y0, y1)
                        .forEach(col -> action.accept(row, col)));
    }
    public void forEachRanged(List<? extends List<V>> grid, BiConsumer<Integer, Integer> action) {
        IntStream.range(0, grid.size())
                .forEach(row -> IntStream.range(0, grid.get(0).size())
                        .forEach(col -> action.accept(row, col)));
    }

}
