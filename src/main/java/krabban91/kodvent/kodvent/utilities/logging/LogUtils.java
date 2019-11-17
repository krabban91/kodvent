package krabban91.kodvent.kodvent.utilities.logging;

import java.util.List;

public class LogUtils {

    public static String tiles(List<? extends List<? extends Loggable>> lights) {
        return lights.stream()
                .map(l -> l.stream()
                        .map(Loggable::showTile)
                        .reduce("", (l1, l2) -> l1 + l2, (l1, l2) -> "" + l1 + l2))
                .reduce((l1, l2) -> l1 + "\n" + l2)
                .orElse("");
    }
}
