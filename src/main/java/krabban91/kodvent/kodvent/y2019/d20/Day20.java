package krabban91.kodvent.kodvent.y2019.d20;

import krabban91.kodvent.kodvent.utilities.Grid;
import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.search.Graph;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day20 {
    List<String> in;

    public Day20() {
        System.out.println("::: Starting Day 20 :::");
        String inputPath = "y2019/d20/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        final Path3D path = getDistanceToPoint3D(false);
        return path.cost();
    }

    public long getPart2() {
        final Path3D search = getDistanceToPoint3D(true);
        return search.cost();
    }

    private Path3D getDistanceToPoint3D(boolean isRecursingMaze) {
        //map the maze
        final Map<Point, Character> map = IntStream.range(0, in.size()).mapToObj(y -> IntStream.range(0, in.get(y).length())
                .mapToObj(x -> Map.entry(new Point(x, y), in.get(y).charAt(x))).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //locate portals
        final Map<Point, String> portalGates = getPortals(map);

        final Point3D start = findGate(portalGates, "AA");
        final Point3D target = findGate(portalGates, "ZZ");

        PriorityQueue<Path3D> unchecked = new PriorityQueue<>(Comparator.comparingInt(Path3D::heuristic));
        Map<Point3D, Path3D> checked = new HashMap<>();
        unchecked.add(new Path3D(start, target));
        Graph<Path3D, Edge3D, Point3D> graph = new Graph<>();
        Map<Point3D, Collection<Edge3D>> network3D = getNetwork3DLayer(map, portalGates, 0, isRecursingMaze);
        return graph.search(unchecked, (p, net) -> this.addNext3D(p, net, unchecked, checked, map, portalGates), network3D);
    }

    private Point3D findGate(Map<Point, String> portalGates, String aa) {
        return portalGates.entrySet().stream().filter(e -> e.getValue().equals(aa)).findFirst().map(Map.Entry::getKey).map(p -> new Point3D(p.x, p.y, 0)).get();
    }

    private Map<Point, String> getPortals(Map<Point, Character> map) {
        final List<Map.Entry<Point, Character>> portalPoints = map.entrySet().stream().filter(this::isUpperCase)
                .collect(Collectors.toList());
        final Grid<Character> grid = new Grid<>(in.stream().map(s -> s.chars().mapToObj(i -> (char) i).collect(Collectors.toList())).collect(Collectors.toList()));
        return portalPoints.stream().map(e -> grid.getSurroundingTilesWithPoints(e.getKey().y, e.getKey().x, true))
                .map(this::getPortalGate)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<Point, String> getPortalGate(List<Map.Entry<Point, Character>> entries) {
        StringBuilder b = new StringBuilder();
        entries.stream().filter(this::isUpperCase).collect(Collectors.toList()).forEach(e -> b.append(e.getValue()));
        return entries.stream()
                .filter(e -> e.getValue().equals('.'))
                .findAny()
                .map(Map.Entry::getKey)
                .map(point1 -> Map.entry(point1, b.toString()))
                .orElse(null);
    }

    private Map<Point3D, Collection<Edge3D>> getNetwork3DLayer(Map<Point, Character> map, Map<Point, String> portalGates, int layer, boolean multiLevel) {
        final Map<Point, Character> walkableArea = map.entrySet().stream().filter(e -> e.getValue().equals('.')).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<Point3D> walkableAreaIn3D = walkableArea.keySet().stream().map(p -> new Point3D(p.x, p.y, layer)).collect(Collectors.toList());
        // filter out portals first.
        int maxX = map.keySet().stream().map(p -> p.x).max(Comparator.comparingInt(x -> x)).orElse(0);
        int maxY = map.keySet().stream().map(p -> p.y).max(Comparator.comparingInt(y -> y)).orElse(0);
        // outer portals
        Map<Point, String> outer = portalGates.entrySet().stream()
                .filter(e -> isOuterRim(maxX, maxY, e))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        List<Point3D> filteredPoints = filterWalkableArea(multiLevel, walkableAreaIn3D, outer);

        return filteredPoints.stream().collect(Collectors.toMap(point -> point, point -> {
            final ArrayList<Edge3D> steps = new ArrayList<>();
            Point flattened = new Point(point.getX(), point.getY());
            
            // normal paths
            Point3D.getDirections()
                    .stream()
                    .map(p-> p.add(point))
                    .filter(p-> walkableArea.containsKey(new Point(p.getX(), p.getY())))
                    .filter(filteredPoints::contains)
                    .forEach(p-> steps.add(new Edge3D(point, p)));
            // portals
            if (portalGates.containsKey(flattened)) {
                final String s = portalGates.get(flattened);
                boolean isOuter = outer.containsKey(flattened);
                portalGates.entrySet().stream()
                        .filter(g -> g.getValue().equals(s))
                        .filter(g -> !g.getKey().equals(flattened))
                        .findAny()
                        .map(Map.Entry::getKey)
                        .map(portalExit -> new Point3D(portalExit.x, portalExit.y, point.getZ() + (multiLevel ? (isOuter ? -1 : 1) : 0)))
                        .ifPresent(to -> steps.add(new Edge3D(point, to)));

            }
            
            return steps;
        }));
    }

    private List<Point3D> filterWalkableArea(boolean multiLevel, List<Point3D> walkableAreaIn3D, Map<Point, String> outer) {
        List<Point3D> filteredPoints;
        if (multiLevel) {
            filteredPoints = walkableAreaIn3D.stream().filter(e -> {
                Point flattened = new Point(e.getX(), e.getY());
                if (outer.containsKey(flattened)) {
                    String s = outer.get(flattened);
                    boolean isMazeExits = s.equals("AA") || s.equals("ZZ");
                    if (e.getZ() == 0) {
                        return isMazeExits;
                    } else {
                        return !isMazeExits;
                    }
                }
                return true;
            }).collect(Collectors.toList());

        } else {
            filteredPoints = walkableAreaIn3D;
        }
        return filteredPoints;
    }

    private boolean isOuterRim(int maxX, int maxY, Map.Entry<Point, String> e) {
        return (e.getKey().x >= 0 && e.getKey().x <= 2) || (e.getKey().x >= maxX - 2 && e.getKey().x <= maxX)
                || (e.getKey().y >= 0 && e.getKey().y <= 2) || (e.getKey().y >= maxY - 2 && e.getKey().y <= maxY);
    }

    private Collection<Path3D> addNext3D(Path3D distanceToPoint, Map<Point3D, Collection<Edge3D>> network, PriorityQueue<Path3D> unchecked, Map<Point3D, Path3D> checked, Map<Point, Character> map, final Map<Point, String> portalGates) {
        checked.putIfAbsent(distanceToPoint.destination(), distanceToPoint);
        int z = distanceToPoint.destination().getZ();
        if (z < 0) {
            return Collections.emptyList();
        }
        if (!network.containsKey(distanceToPoint.destination())) {
            network.putAll(getNetwork3DLayer(map, portalGates, z, true));
        }
        return network.get(distanceToPoint.destination())
                .stream()
                .map(w -> new Path3D(distanceToPoint, w, () -> z * z))
                .filter(d -> !distanceToPoint.hasVisited(d.destination()))
                .filter(d -> !checked.containsKey(d.destination()))
                .filter(d -> unchecked.stream().noneMatch(p -> p.destination().equals(d.destination())))
                .collect(Collectors.toList());
    }

    public boolean isUpperCase(Map.Entry<Point, Character> e) {
        return e.getValue().compareTo('A') >= 0 && e.getValue().compareTo('Z') <= 0;
    }


    public void readInput(String inputPath) {

        in = Input.getLines(inputPath);
    }
}
