package krabban91.kodvent.kodvent.y2019.d24;

import krabban91.kodvent.kodvent.utilities.Grid;
import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day24 {
    List<List<BugTile>> in;
    boolean debug;

    public Day24() {
        System.out.println("::: Starting Day 24 :::");
        String inputPath = "y2019/d24/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        Set<Grid<BugTile>> history = new HashSet<>();
        Grid<BugTile> current = new Grid<>(in);
        long minute = 0;
        while (!history.contains(current)) {
            logState(current, minute);
            history.add(current);
            minute++;
            current = nextState(current);
        }
        logState(current, minute);

        List<? extends List<BugTile>> finalCurrent1 = current.getRaw();
        return IntStream.range(0, finalCurrent1.size())
                .mapToLong(y -> IntStream.range(0, finalCurrent1.get(y).size())
                        .mapToLong(x -> (long) ((finalCurrent1.get(y).get(x).isBug() ? 1 : 0) * Math.pow(2, (y * finalCurrent1.size() + x))))
                        .sum())
                .sum();
    }

    private void logState(Grid<BugTile> current, long minute) {
        if (debug) {
            System.out.println("Minute " + minute);
            System.out.println(LogUtils.tiles(current.getRaw()));
        }
    }

    public long getPart2() {
        long limit = 200;
        return bugsAfter(limit);

    }

    public long bugsAfter(long minutes) {
        Map<Point3D, BugTile> current = IntStream.range(0, in.size()).mapToObj(y -> IntStream.range(0, in.get(y).size()).mapToObj(x -> Map.entry(new Point3D(x, y, 0), in.get(y).get(x))).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        long minute = 0;
        while (minute < minutes) {
            minute++;
            current = nextState(current);
        }
        return current.entrySet().stream().filter(e -> e.getValue().isBug()).count();
    }

    private Map<Point3D, BugTile> freshTile(int level) {
        return IntStream.range(0, in.size()).mapToObj(y -> IntStream.range(0, in.get(y).size()).mapToObj(x -> Map.entry(new Point3D(x, y, level), new BugTile(false)))
                .filter(e -> nonCenterTile(e.getKey().getY(), e.getKey().getX()))
                .collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Grid<BugTile> nextState(Grid<BugTile> grid) {
        return grid.map((b, p) -> b.nextState(grid.getAdjacentTiles(p.y, p.x)));
    }

    private Map<Point3D, BugTile> nextState(Map<Point3D, BugTile> current) {
        HashMap<Point3D, BugTile> next = padWithPlutonianLayers(current);
        int maxZ = current.keySet().stream().mapToInt(Point3D::getZ).max().orElse(0);
        int minZ = current.keySet().stream().mapToInt(Point3D::getZ).min().orElse(0);
        return next.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().nextState(getAdjacentTiles(next, e.getKey(), maxZ, minZ))));
    }

    private HashMap<Point3D, BugTile> padWithPlutonianLayers(Map<Point3D, BugTile> current) {
        HashMap<Point3D, BugTile> next = new HashMap<>(current);
        int maxZ = current.keySet().stream().mapToInt(Point3D::getZ).max().orElse(0);
        int minZ = current.keySet().stream().mapToInt(Point3D::getZ).min().orElse(0);
        int finalMaxZ = maxZ;
        boolean addLayerUpwards = current.entrySet().stream()
                .filter(e -> e.getKey().getZ() == finalMaxZ)
                .filter(e -> e.getValue().isBug())
                .anyMatch(e -> e.getKey().getX() == 0 || e.getKey().getY() == 0 || e.getKey().getX() == 4 || e.getKey().getY() == 4);
        int finalMinZ = minZ;
        boolean addLayerDownwards = current.entrySet().stream()
                .filter(e -> e.getKey().getZ() == finalMinZ)
                .filter(e -> e.getValue().isBug())
                .anyMatch(e -> e.getKey().getX() == 1 || e.getKey().getY() == 1 || e.getKey().getX() == 3 || e.getKey().getY() == 3);
        if (addLayerDownwards) {
            minZ = minZ - 1;
            Map<Point3D, BugTile> down = freshTile(minZ);
            next.putAll(down);
        }
        if (addLayerUpwards) {
            maxZ = maxZ + 1;
            Map<Point3D, BugTile> up = freshTile(maxZ);
            next.putAll(up);
        }
        return next;
    }

    private Collection<BugTile> getAdjacentTiles(HashMap<Point3D, BugTile> next, Point3D key, int maxZ, int minZ) {
        Set<Point3D> keys = new HashSet<>();
        if (key.getX() == 0 || key.getY() == 0 || key.getX() == 4 || key.getY() == 4) {
            if (key.getZ() != maxZ) {
                if (key.getX() == 0) {
                    keys.add(new Point3D(1, 2, key.getZ() + 1));
                }
                if (key.getX() == 4) {
                    keys.add(new Point3D(3, 2, key.getZ() + 1));
                }
                if (key.getY() == 0) {
                    keys.add(new Point3D(2, 1, key.getZ() + 1));
                }
                if (key.getY() == 4) {
                    keys.add(new Point3D(2, 3, key.getZ() + 1));
                }
            }
        }
        if (key.getX() == 1 || key.getY() == 1 || key.getX() == 3 || key.getY() == 3) {
            if (key.getZ() != minZ) {
                if (key.getY() == 2 && key.getX() == 1) {
                    keys.add(new Point3D(0, 0, key.getZ() - 1));
                    keys.add(new Point3D(0, 1, key.getZ() - 1));
                    keys.add(new Point3D(0, 2, key.getZ() - 1));
                    keys.add(new Point3D(0, 3, key.getZ() - 1));
                    keys.add(new Point3D(0, 4, key.getZ() - 1));
                }
            }
            if (key.getY() == 2 && key.getX() == 3) {
                keys.add(new Point3D(4, 0, key.getZ() - 1));
                keys.add(new Point3D(4, 1, key.getZ() - 1));
                keys.add(new Point3D(4, 2, key.getZ() - 1));
                keys.add(new Point3D(4, 3, key.getZ() - 1));
                keys.add(new Point3D(4, 4, key.getZ() - 1));
            }
            if (key.getY() == 1 && key.getX() == 2) {
                keys.add(new Point3D(0, 0, key.getZ() - 1));
                keys.add(new Point3D(1, 0, key.getZ() - 1));
                keys.add(new Point3D(2, 0, key.getZ() - 1));
                keys.add(new Point3D(3, 0, key.getZ() - 1));
                keys.add(new Point3D(4, 0, key.getZ() - 1));

            }
            if (key.getY() == 3 && key.getX() == 2) {
                keys.add(new Point3D(0, 4, key.getZ() - 1));
                keys.add(new Point3D(1, 4, key.getZ() - 1));
                keys.add(new Point3D(2, 4, key.getZ() - 1));
                keys.add(new Point3D(3, 4, key.getZ() - 1));
                keys.add(new Point3D(4, 4, key.getZ() - 1));
            }
        }
        keys.add(new Point3D(key.getX() - 1, key.getY(), key.getZ()));
        keys.add(new Point3D(key.getX() + 1, key.getY(), key.getZ()));
        keys.add(new Point3D(key.getX(), key.getY() - 1, key.getZ()));
        keys.add(new Point3D(key.getX(), key.getY() + 1, key.getZ()));
        return keys.stream().filter(e -> nonCenterTile(e.getX(), e.getY()))
                .filter(next::containsKey)
                .map(next::get).collect(Collectors.toList());
    }

    private boolean nonCenterTile(int x, int y) {
        return !(x == 2 && y == 2);
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).stream().map(s -> s.chars().mapToObj(c -> new BugTile((char) c == '#')).collect(Collectors.toList())).collect(Collectors.toList());
    }
}
