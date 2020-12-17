package krabban91.kodvent.kodvent.utilities.logging;

import krabban91.kodvent.kodvent.utilities.grid.CubeGrid;
import krabban91.kodvent.kodvent.utilities.grid.Grid;
import krabban91.kodvent.kodvent.utilities.grid.HyperCubeGrid;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

public class LogUtils<V> {


    public static String tiles(List<? extends List<? extends Loggable>> lights) {
        return lights.stream()
                .map(l -> l.stream()
                        .map(Loggable::showTile)
                        .reduce("", (l1, l2) -> l1 + l2, (l1, l2) -> "" + l1 + l2))
                .reduce((l1, l2) -> l1 + "\n" + l2)
                .orElse("");
    }

    public static String tiles(Grid<? extends Loggable> grid) {
        return tiles(grid.getRaw());
    }

    public static String tiles(CubeGrid<? extends Loggable> grid) {
        return IntStream.range(0,grid.depth())
                .mapToObj(z -> String.format("z=%d\n%s",z, tiles(grid.getRaw().get(z))))
                .reduce((l,r) -> l+"\n"+r)
                .orElse("");
    }
    public static String tiles(HyperCubeGrid<? extends Loggable> grid) {
        return IntStream.range(0,grid.metaDepth())
        .mapToObj(w -> IntStream.range(0,grid.depth())
                .mapToObj(z -> String.format("w=%d, z=%d\n%s",w, z, tiles(grid.getRaw().get(w).get(z))))
                .reduce((l,r) -> l+"\n"+r)
                .orElse(""))
                .reduce((l,r) -> l+"\n"+r)
                .orElse("");
    }


    public String mapToText(Map<Point, V> map, Function<V, String> valueMapper) {
        int minx = map.keySet().stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = map.keySet().stream().mapToInt(p -> p.x).max().orElse(0);
        int miny = map.keySet().stream().mapToInt(p -> p.y).min().orElse(0);
        int maxy = map.keySet().stream().mapToInt(p -> p.y).max().orElse(0);
        StringBuilder b = new StringBuilder();
        for (int y = miny; y <= maxy; y++) {
            for (int x = minx; x <= maxX; x++) {
                b.append(valueMapper.apply(map.get(new Point(x, y))));
            }
            b.append("\n");
        }
        return b.toString();
    }
}
