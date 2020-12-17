package krabban91.kodvent.kodvent.utilities.grid;

import krabban91.kodvent.kodvent.utilities.Point3D;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CubeGrid<V> {

    private final List<? extends List<? extends List<V>>> raw;

    public CubeGrid(List<? extends List<? extends List<V>>> cubeGrid) {
        this.raw = cubeGrid;
    }

    public int depth() {
        return this.raw.size();
    }

    public int height() {
        return this.raw.get(0).size();
    }

    public int width() {
        return this.raw.get(0).get(0).size();
    }

    public List<V> getSurroundingTiles(Point3D p) {
        return getNearestTilesOfSurroundingDirections(p, v -> true);
    }

    public List<V> getNearestTilesOfSurroundingDirections(Point3D point3D, Predicate<V> predicate) {
        List<Integer> xs = List.of(-1, 0, 1);
        List<Point> ys = xs.stream().map(v -> List.of(new Point(v, -1), new Point(v, 0), new Point(v, 1))).flatMap(List::stream).collect(Collectors.toList());
        List<Point3D> zs = ys.stream().map(p -> List.of(new Point3D(p.x, p.y, -1), new Point3D(p.x, p.y, 0), new Point3D(p.x, p.y, 1))).flatMap(List::stream).collect(Collectors.toList());
        return zs.stream().filter(p -> !p.equals(new Point3D(0, 0, 0)))
                .map(p -> getNearestInDirection(point3D, p, predicate))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<V> getNearestInDirection(Point3D point, Point3D direction, Predicate<V> predicate) {
        point = point.add(direction);
        Optional<V> tile = this.get(point);
        while (tile.isPresent() && !predicate.test(tile.get())) {
            point = point.add(direction);
            tile = this.get(point);
        }
        return tile;
    }

    public CubeGrid<V> map(BiFunction<V, Point3D, V> function) {
        return new CubeGrid<>(IntStream.range(0, raw.size())
                .mapToObj(z -> IntStream.range(0, raw.get(0).size())
                        .mapToObj(y -> IntStream.range(0, raw.get(z).get(y).size())
                                .mapToObj(x -> function.apply(this.get(x, y, z).get(), new Point3D(x, y, z)))
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    public CubeGrid<V> expandOneStep(Function<Point3D, V> generator) {
        return new CubeGrid<>(IntStream.range(-1, raw.size() + 1)
                .mapToObj(z -> IntStream.range(-1, raw.get(0).size() + 1)
                        .mapToObj(y -> IntStream.range(-1, raw.get(0).get(0).size() + 1)
                                .mapToObj(x -> this.get(x, y, z).orElseGet(() -> generator.apply(new Point3D(x, y, z))))
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    public Optional<V> get(Point3D p) {
        return this.get(p.getX(), p.getY(), p.getZ());
    }

    public Optional<V> get(int x, int y, int z) {
        return (x < 0 || x >= width() || y < 0 || y >= height() || z < 0 || z >= depth()) ?
                Optional.empty() :
                Optional.of(this.raw.get(z).get(y).get(x));
    }

    public long sum(ToLongFunction<V> valueMethod) {
        return this.raw.stream().mapToLong(ll -> ll.stream().mapToLong(l -> l.stream().mapToLong(valueMethod).sum()).sum()).sum();
    }

    public List<? extends List<? extends List<V>>> getRaw() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CubeGrid<?> grid = (CubeGrid<?>) o;

        return raw.equals(grid.raw);
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

}
