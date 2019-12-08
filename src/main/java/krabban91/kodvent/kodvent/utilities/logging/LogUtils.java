package krabban91.kodvent.kodvent.utilities.logging;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Function;

public class LogUtils {

    public static String tiles(List<? extends List<? extends Loggable>> lights) {
        return lights.stream()
                .map(l -> l.stream()
                        .map(Loggable::showTile)
                        .reduce("", (l1, l2) -> l1 + l2, (l1, l2) -> "" + l1 + l2))
                .reduce((l1, l2) -> l1 + "\n" + l2)
                .orElse("");
    }
    public static String mapToText(Map<Point, Integer> map, Function<Integer, String> valueMapper) {
        int width = map.keySet().stream().mapToInt(p -> p.x + 1).max().orElse(0);
        int height = map.keySet().stream().mapToInt(p -> p.y + 1).max().orElse(0);
        StringBuilder b = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Integer obj = map.get(new Point(x, y));
                b.append(obj == 0 ? " " : "*");
            }
            b.append("\n");
        }
        return b.toString();
    }
}
