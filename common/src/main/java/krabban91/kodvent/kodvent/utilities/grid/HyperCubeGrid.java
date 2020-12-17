package krabban91.kodvent.kodvent.utilities.grid;

import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.TimePoint;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HyperCubeGrid<V> {

    private final List<? extends List<? extends List<? extends List<V>>>> raw;

    public HyperCubeGrid(List<? extends List<? extends List<? extends List<V>>>> cubeGrid) {
        this.raw = cubeGrid;
    }

    public int metaDepth() {
        return this.raw.size();
    }

    public int depth() {
        return this.raw.get(0).size();
    }

    public int height() {
        return this.raw.get(0).get(0).size();
    }

    public int width() {
        return this.raw.get(0).get(0).get(0).size();
    }


    public List<V> getSurroundingTiles(TimePoint p) {
        return getNearestTilesOfSurroundingDirections(p, v -> true);
    }


    public List<V> getNearestTilesOfSurroundingDirections(TimePoint from, Predicate<V> predicate) {
        List<Integer> xs = List.of(-1, 0, 1);
        List<Point> ys = xs.stream().map(v -> List.of(new Point(v, -1), new Point(v, 0), new Point(v, 1))).flatMap(List::stream).collect(Collectors.toList());
        List<Point3D> zs = ys.stream().map(p -> List.of(new Point3D(p.x, p.y, -1), new Point3D(p.x, p.y, 0), new Point3D(p.x, p.y, 1))).flatMap(List::stream).collect(Collectors.toList());
        List<TimePoint> ws = zs.stream().map(p -> List.of(new TimePoint(p.getX(), p.getY(), p.getZ(), -1), new TimePoint(p.getX(), p.getY(), p.getZ(), 0), new TimePoint(p.getX(), p.getY(), p.getZ(), 1))).flatMap(List::stream).collect(Collectors.toList());
        return ws.stream().filter(p -> !p.equals(new TimePoint(0, 0, 0, 0)))
                .map(p -> getNearestInDirection(from, p, predicate))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<V> getNearestInDirection(TimePoint point, TimePoint direction, Predicate<V> predicate) {
        point = point.add(direction);
        Optional<V> tile = this.get(point);
        while (tile.isPresent() && !predicate.test(tile.get())) {
            point = point.add(direction);
            tile = this.get(point);
        }
        return tile;
    }

    public HyperCubeGrid<V> map(BiFunction<V, TimePoint, V> function) {
        return new HyperCubeGrid<>(IntStream.range(0, raw.size())
                .mapToObj(w -> IntStream.range(0, raw.get(0).size())
                        .mapToObj(z -> IntStream.range(0, raw.get(0).get(0).size())
                                .mapToObj(y -> IntStream.range(0, raw.get(0).get(0).get(0).size())
                                        .mapToObj(x -> function.apply(this.get(x, y, z, w).get(), new TimePoint(x, y, z, w)))
                                        .collect(Collectors.toList()))
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    public HyperCubeGrid<V> expandOneStep(Function<TimePoint, V> generator) {
        return new HyperCubeGrid<>(IntStream.range(-1, raw.size() + 1)
                .mapToObj(w -> IntStream.range(-1, raw.get(0).size() + 1)
                        .mapToObj(z -> IntStream.range(-1, raw.get(0).get(0).size() + 1)
                                .mapToObj(y -> IntStream.range(-1, raw.get(0).get(0).get(0).size() + 1)
                                        .mapToObj(x -> this.get(x, y, z, w).orElseGet(() -> generator.apply(new TimePoint(x, y, z, w))))
                                        .collect(Collectors.toList()))
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }

    public Optional<V> get(TimePoint p) {
        return this.get(p.getX(), p.getY(), p.getZ(), p.getW());
    }

    public Optional<V> get(int x, int y, int z, int w) {
        return (x < 0 || x >= width() || y < 0 || y >= height() || z < 0 || z >= depth() || w < 0 || w >= metaDepth()) ?
                Optional.empty() :
                Optional.of(this.raw.get(w).get(z).get(y).get(x));
    }

    public long sum(ToLongFunction<V> valueMethod) {
        return this.raw.stream().mapToLong(lll -> lll.stream().mapToLong(ll -> ll.stream().mapToLong(l -> l.stream().mapToLong(valueMethod).sum()).sum()).sum()).sum();
    }

    public List<? extends List<? extends List<? extends List<V>>>> getRaw() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HyperCubeGrid<?> grid = (HyperCubeGrid<?>) o;

        return raw.equals(grid.raw);
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

}
