package krabban91.kodvent.kodvent.day17;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Reservoir {

    public static final Point springLocation = new Point(500, 0);

    Map<Point, SquareMeter> map;
    int maxY;
    int minY;
    int maxX;
    int minX;

    public Reservoir(Stream<ClayVein> clayVeinStream) {
        map = clayVeinStream.map(ClayVein::getDeposits).flatMap(List::stream).collect(Collectors.toMap(e -> e, e -> SquareMeter.CLAY, (l, r) -> l));
        minY = map.keySet().stream().min(Comparator.comparingDouble(Point::getY)).get().y;
        maxY = map.keySet().stream().max(Comparator.comparingDouble(Point::getY)).get().y;
        minX = map.keySet().stream().min(Comparator.comparingDouble(Point::getX)).get().x;
        maxX = map.keySet().stream().max(Comparator.comparingDouble(Point::getX)).get().x;
    }

    public long countFilledSquareMeters() {
        this.flowDown(springLocation);
        visualizeReservoir();
        return map.entrySet().stream()
                .filter(e -> e.getKey().y >= minY && e.getKey().y <= maxY)
                .filter(e -> e.getValue().equals(SquareMeter.WATER) || e.getValue().equals(SquareMeter.FLOWING_WATER)).count();
    }

    public long countRetainedSquareMeters() {

        this.flowDown(springLocation);

        return map.entrySet().stream()
                .filter(e -> e.getKey().y >= minY && e.getKey().y <= maxY)
                .filter(e -> e.getValue().equals(SquareMeter.WATER)).count();
    }

    private void visualizeReservoir() {
        StringBuilder builder = new StringBuilder();
        IntStream.rangeClosed(minY, maxY).forEach(y -> {
            builder.append(y + "\t");
            IntStream.rangeClosed(minX, maxX).forEach(x -> {
                SquareMeter squareMeter = map.get(new Point(x, y));
                if (squareMeter == null || squareMeter.equals(SquareMeter.SAND)) {
                    builder.append('.');
                } else if (squareMeter.equals(SquareMeter.CLAY)) {
                    builder.append('#');
                } else if (squareMeter.equals(SquareMeter.WATER)) {
                    builder.append('~');
                } else if (squareMeter.equals(SquareMeter.FLOWING_WATER)) {
                    builder.append('|');
                }
            });
            builder.append('\n');
        });
        System.out.print("\r" + builder.toString());
    }

    private void flowDown(Point initialPoint) {
        Point point = initialPoint;
        while (point.y <= maxY) {
            if (waterOrGroundBelow(point)) {
                Point tileWithWallToLeft = findTileWithWallToLeft(point);
                Point tileWithWallToRight = findTileWithWallToRight(point);
                while (tileWithWallToLeft != null && tileWithWallToRight != null) {
                    Point finalPoint = point;
                    IntStream.rangeClosed(tileWithWallToLeft.x, tileWithWallToRight.x).mapToObj(x -> new Point(x, finalPoint.y)).forEach(p -> map.put(p, SquareMeter.WATER));

                    point = new Point(point.x, point.y - 1);
                    tileWithWallToLeft = findTileWithWallToLeft(point);
                    tileWithWallToRight = findTileWithWallToRight(point);
                }
                if (tileWithWallToLeft != null) {
                    Point finalPoint = point;
                    IntStream.range(tileWithWallToLeft.x, point.x).mapToObj(x -> new Point(x, finalPoint.y)).forEach(p -> map.put(p, SquareMeter.FLOWING_WATER));
                    flowRight(point);
                } else if (tileWithWallToRight != null) {
                    Point finalPoint = point;
                    IntStream.rangeClosed(point.x + 1, tileWithWallToRight.x).mapToObj(x -> new Point(x, finalPoint.y)).forEach(p -> map.put(p, SquareMeter.FLOWING_WATER));
                    flowLeft(point);
                } else {
                    flowLeft(point);
                    flowRight(point);
                }
                break;
            } else if (flowingBelow(point)) {
                map.put(point, SquareMeter.FLOWING_WATER);
                break;
            } else {
                map.put(point, SquareMeter.FLOWING_WATER);
                point = new Point(point.x, point.y + 1);
            }
        }
    }

    private void flowLeft(Point initialPoint) {
        Point point = initialPoint;
        while (waterOrGroundBelow(point)) {
            if (occupiedTile(map.get(point))) {
                return;
            }
            map.put(point, SquareMeter.FLOWING_WATER);
            point = new Point(point.x - 1, point.y);
        }
        flowDown(point);
    }

    private void flowRight(Point initialPoint) {
        Point point = initialPoint;
        while (waterOrGroundBelow(point)) {
            if (occupiedTile(map.get(point))) {
                return;
            }
            map.put(point, SquareMeter.FLOWING_WATER);
            point = new Point(point.x + 1, point.y);
        }
        flowDown(point);
    }

    private boolean flowingBelow(Point point) {
        SquareMeter squareMeter = map.get(new Point(point.x, point.y + 1));
        return flowTile(squareMeter);
    }

    private boolean waterOrGroundBelow(Point point) {
        SquareMeter squareMeter = map.get(new Point(point.x, point.y + 1));
        return occupiedTile(squareMeter);
    }

    private boolean flowTile(SquareMeter squareMeter) {
        return squareMeter != null &&
                squareMeter.equals(SquareMeter.FLOWING_WATER);
    }

    private boolean clayTile(SquareMeter squareMeter) {
        return squareMeter != null &&
                squareMeter.equals(SquareMeter.CLAY);
    }

    private boolean occupiedTile(SquareMeter squareMeter) {
        return squareMeter != null &&
                (squareMeter.equals(SquareMeter.WATER) ||
                        squareMeter.equals(SquareMeter.CLAY));
    }

    private Point findTileWithWallToLeft(Point point) {
        SquareMeter m = map.get(new Point(point.x - 1, point.y + 1));
        if (occupiedTile(m)) {
            Point left = new Point(point.x - 1, point.y);
            m = map.get(left);
            if (clayTile(m)) {
                return point;
            }
            return findTileWithWallToLeft(left);
        }
        return null;
    }

    private Point findTileWithWallToRight(Point point) {
        SquareMeter m = map.get(new Point(point.x + 1, point.y + 1));
        if (occupiedTile(m)) {
            Point right = new Point(point.x + 1, point.y);
            m = map.get(right);
            if (clayTile(m)) {
                return point;
            }
            return findTileWithWallToRight(right);
        }
        return null;
    }
}
