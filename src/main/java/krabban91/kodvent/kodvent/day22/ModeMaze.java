package krabban91.kodvent.kodvent.day22;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModeMaze {

    int depth;
    Point target;
    Point entrance;

    Map<Point, Integer> geoIndices = new HashMap<>();
    Map<Point, Integer> erosionLevels = new HashMap<>();
    Map<Point, Integer> dangerMap = new HashMap<>();
    Map<Point, Region> regionMap;

    public ModeMaze(List<String> input) {
        depth = Integer.parseInt(input.get(0).split(": ")[1]);
        String[] split = input.get(1).split(": ")[1].split(",");
        entrance = new Point(0, 0);
        target = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        regionMap = getRegionMap();
    }

    private Map<Point, Region> getRegionMap() {
        return IntStream.rangeClosed(0, target.y + 100)
                .mapToObj(y -> IntStream.rangeClosed(0, target.x + 100)
                        .mapToObj(x -> {
                            int geoIndex = 0;
                            Point current = new Point(x, y);
                            if (current.x == entrance.x && current.y == entrance.y) {

                            } else if (current.x == target.x && current.y == target.y) {

                            } else if (current.y == entrance.y) {
                                geoIndex = current.x * 16807;
                            } else if (current.x == entrance.x) {
                                geoIndex = current.y * 48271;
                            } else {
                                geoIndex = this.erosionLevels.get(new Point(current.x - 1, current.y)) * this.erosionLevels.get(new Point(current.x, current.y - 1));
                            }
                            this.geoIndices.put(current, geoIndex);
                            int erosionLevel = (geoIndex + depth) % 20183;
                            this.erosionLevels.put(current, erosionLevel);
                            int danger = erosionLevel % 3;
                            this.dangerMap.put(current, danger);
                            switch (danger) {
                                case 0:
                                    return new Region(current, RegionType.ROCKY);
                                case 1:
                                    return new Region(current, RegionType.WET);
                                default:
                                    return new Region(current, RegionType.NARROW);
                            }
                        }).collect(Collectors.toList()))
                .flatMap(List::stream)

                .collect(Collectors.toMap(r -> r.getPoint(), r -> r));
    }

    public long estimateDangerLevel() {

        return this.dangerMap.entrySet().stream()
                .filter(e -> e.getKey().x >= 0 && e.getKey().x <= target.x)
                .filter(e -> e.getKey().y >= 0 && e.getKey().y <= target.y)
                .map(e -> e.getValue()).reduce(0, Integer::sum);
    }

    public long fewestMinutesToReachTarget() {
        return 0;
    }
}
