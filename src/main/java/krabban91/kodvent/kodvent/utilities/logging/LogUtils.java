package krabban91.kodvent.kodvent.utilities.logging;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LogUtils<V> {


    public static String tiles(List<? extends List<? extends Loggable>> lights) {
        return lights.stream()
                .map(l -> l.stream()
                        .map(Loggable::showTile)
                        .reduce("", (l1, l2) -> l1 + l2, (l1, l2) -> "" + l1 + l2))
                .reduce((l1, l2) -> l1 + "\n" + l2)
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
