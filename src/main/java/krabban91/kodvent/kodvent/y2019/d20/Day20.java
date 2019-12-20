package krabban91.kodvent.kodvent.y2019.d20;

import krabban91.kodvent.kodvent.utilities.Grid;
import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.utilities.search.Graph;
import krabban91.kodvent.kodvent.y2018.d15.DistanceToPoint;
import krabban91.kodvent.kodvent.y2018.d15.Step;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
        //map the maze
        final Map<Point, Character> map = IntStream.range(0, in.size()).mapToObj(y -> IntStream.range(0, in.get(y).length())
                .mapToObj(x -> Map.entry(new Point(x, y), in.get(y).charAt(x))).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //locate portals
        final List<Map.Entry<Point, Character>> portalPoints = map.entrySet().stream().filter(this::isUpperCase)
                .collect(Collectors.toList());
        final Grid<Character> grid = new Grid<>(in.stream().map(s -> s.chars().mapToObj(i -> (char) i).collect(Collectors.toList())).collect(Collectors.toList()));
        final Map<Point, String> portalGates = portalPoints.stream().map(e -> grid.getSurroundingTilesWithPoints(e.getKey().y, e.getKey().x, true)).map(this::getPortalGate).filter(Objects::nonNull).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        final Point start = portalGates.entrySet().stream().filter(e -> e.getValue().equals("AA")).findFirst().map(Map.Entry::getKey).get();
        final Point target = portalGates.entrySet().stream().filter(e -> e.getValue().equals("ZZ")).findFirst().map(Map.Entry::getKey).get();

        PriorityQueue<DistanceToPoint> unchecked = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint::heuristic));
        Map<Point, DistanceToPoint> checked = new HashMap<>();
        unchecked.add(new DistanceToPoint(start, target));
        Graph<DistanceToPoint, Step, Point> graph = new Graph<>();
        final DistanceToPoint search = graph.search(unchecked, (p, net) -> this.addNext(p, net, unchecked, checked), getNetwork(map, portalGates));


        System.out.println(new LogUtils<Character>().mapToText(map, v -> v == null ? " " : v.toString()));
        return search.cost();
    }


    public long getPart2() {
        //map the maze
        final Map<Point, Character> map = IntStream.range(0, in.size()).mapToObj(y -> IntStream.range(0, in.get(y).length())
                .mapToObj(x -> Map.entry(new Point(x, y), in.get(y).charAt(x))).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //locate portals
        final List<Map.Entry<Point, Character>> portalPoints = map.entrySet().stream().filter(this::isUpperCase)
                .collect(Collectors.toList());
        final Grid<Character> grid = new Grid<>(in.stream().map(s -> s.chars().mapToObj(i -> (char) i).collect(Collectors.toList())).collect(Collectors.toList()));
        final Map<Point, String> portalGates = portalPoints.stream().map(e -> grid.getSurroundingTilesWithPoints(e.getKey().y, e.getKey().x, true)).map(this::getPortalGate).filter(Objects::nonNull).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        final Point start = portalGates.entrySet().stream().filter(e -> e.getValue().equals("AA")).findFirst().map(Map.Entry::getKey).get();
        final Point target = portalGates.entrySet().stream().filter(e -> e.getValue().equals("ZZ")).findFirst().map(Map.Entry::getKey).get();
        Point3D start3D = new Point3D(start.x, start.y, 0);
        Point3D target3D = new Point3D(target.x, target.y, 0);
        PriorityQueue<DistanceToPoint3D> unchecked = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint3D::heuristic));
        Map<Point3D, DistanceToPoint3D> checked = new HashMap<>();
        unchecked.add(new DistanceToPoint3D(start3D, target3D));
        Graph<DistanceToPoint3D, Step3D, Point3D> graph = new Graph<>();
        Map<Point3D, Collection<Step3D>> network3D = getNetwork3DLayer(map, portalGates,0);
        final DistanceToPoint3D search = graph.search(unchecked, (p, net) -> this.addNext3D(p, net, unchecked, checked, map, portalGates), network3D);

        return search.cost();
    }

    private Map<Point, Collection<Step>> getNetwork(Map<Point, Character> map, Map<Point, String> portalGates) {
        final Map<Point, Character> dots = map.entrySet().stream().filter(e -> e.getValue().equals('.')).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        return dots.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> {
            final ArrayList<Step> steps = new ArrayList<>();
            if (portalGates.containsKey(e.getKey())) {
                final String s = portalGates.get(e.getKey());
                final Optional<Map.Entry<Point, String>> otherGate = portalGates.entrySet().stream().filter(g -> g.getValue().equals(s)).filter(g -> !g.getKey().equals(e.getKey())).findAny();
                otherGate.ifPresent(pointStringEntry -> steps.add(new Step(e.getKey(), pointStringEntry.getKey())));
            }

            Point north = new Point(e.getKey().x, e.getKey().y - 1);
            Point south = new Point(e.getKey().x, e.getKey().y + 1);
            Point east = new Point(e.getKey().x + 1, e.getKey().y);
            Point west = new Point(e.getKey().x - 1, e.getKey().y);
            final List<Point> points = Arrays.asList(north, south, east, west);
            points.forEach(p -> {
                if (dots.containsKey(p)) {
                    steps.add(new Step(e.getKey(), p));
                }
            });
            return steps;
        }));
    }

    private Collection<DistanceToPoint> addNext(DistanceToPoint distanceToPoint, Map<Point, Collection<Step>> network, PriorityQueue<DistanceToPoint> unchecked, Map<Point, DistanceToPoint> checked) {
        checked.putIfAbsent(distanceToPoint.destination(), distanceToPoint);
        return network.get(distanceToPoint.destination())
                .stream()
                .map(w -> new DistanceToPoint(distanceToPoint, w, 0))
                .filter(d -> !distanceToPoint.hasVisited(d.destination()))
                .filter(d -> !checked.containsKey(d.destination()))
                .filter(d -> unchecked.stream().noneMatch(p -> p.destination().equals(d.destination())))
                .collect(Collectors.toList());
    }

    private Map.Entry<Point, String> getPortalGate(List<Map.Entry<Point, Character>> entries) {
        StringBuilder b = new StringBuilder();
        entries.stream().filter(this::isUpperCase).collect(Collectors.toList()).forEach(e -> b.append(e.getValue()));
        final Optional<Point> point = entries.stream().filter(e -> e.getValue().equals('.')).findAny().map(Map.Entry::getKey);
        return point.map(point1 -> Map.entry(point1, b.toString())).orElse(null);
    }

    public boolean isUpperCase3D(Map.Entry<Point3D, Character> e) {
        return e.getValue().compareTo('A') >= 0 && e.getValue().compareTo('Z') <= 0;
    }

    private Map<Point3D, Collection<Step3D>> getNetwork3DLayer(Map<Point, Character> map, Map<Point, String> portalGates, int layer) {
        final Map<Point, Character> dots = map.entrySet().stream().filter(e -> e.getValue().equals('.')).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        List<Point3D> dotPoint3ds = dots.keySet().stream().map(p -> new Point3D(p.x, p.y, layer)).collect(Collectors.toList());
        // filter out portals first.
        int maxX = map.keySet().stream().map(p -> p.x).max(Comparator.comparingInt(x -> x)).orElse(0);
        int maxY = map.keySet().stream().map(p -> p.y).max(Comparator.comparingInt(y -> y)).orElse(0);
        // outer portals
        Map<Point, String> outer = portalGates.entrySet().stream().filter(e -> (e.getKey().x >= 0 && e.getKey().x <= 2) || (e.getKey().x >= maxX - 2 && e.getKey().x <= maxX)
                || (e.getKey().y >= 0 && e.getKey().y <= 2) || (e.getKey().y >= maxY - 2 && e.getKey().y <= maxY)).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        // outer3d
        //Map<Point3D, String> outer3D = IntStream.range(0, 100).mapToObj(z -> outer.entrySet().stream().map(e -> Map.entry(new Point3D(e.getKey().x, e.getKey().y, z), e.getValue())).filter(Objects::nonNull).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // inner portals
        Map<Point, String> inner = portalGates.entrySet().stream().filter(e -> !outer.containsKey(e.getKey())).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        // inner3d
        //Map<Point3D, String> inner3D = IntStream.range(0, 100).mapToObj(z -> inner.entrySet().stream().map(e -> Map.entry(new Point3D(e.getKey().x, e.getKey().y, z), e.getValue())).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<Point3D> filteredPoints = dotPoint3ds.stream().filter(e -> {
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
        return filteredPoints.stream().collect(Collectors.toMap(e -> e, e -> {
            final ArrayList<Step3D> steps = new ArrayList<>();
            Point flattened = new Point(e.getX(), e.getY());
            if (portalGates.containsKey(flattened)) {
                final String s = portalGates.get(flattened);
                final Optional<Map.Entry<Point, String>> otherGate = portalGates.entrySet().stream().filter(g -> g.getValue().equals(s)).filter(g -> !g.getKey().equals(flattened)).findAny();
                boolean isOuter = outer.containsKey(flattened);
                if (otherGate.isPresent()){

                    otherGate.ifPresent(pointStringEntry -> steps.add(new Step3D(e, new Point3D(pointStringEntry.getKey().x, pointStringEntry.getKey().y, e.getZ() + (isOuter ? -1 : 1)))));
                }
            }
            Point3D north = new Point3D(e.getX(), e.getY() - 1, e.getZ());
            Point3D south = new Point3D(e.getX(), e.getY() + 1, e.getZ());
            Point3D east = new Point3D(e.getX() + 1, e.getY(), e.getZ());
            Point3D west = new Point3D(e.getX() - 1, e.getY(), e.getZ());
            final List<Point3D> points = Arrays.asList(north, south, east, west);
            points.forEach(p -> {
                Point flatP = new Point(p.getX(), p.getY());
                if(dots.containsKey(flatP)){
                    if (filteredPoints.contains(p)) {
                        steps.add(new Step3D(e, p));
                    }
                }
            });
            return steps;
        }));
    }

    private Collection<DistanceToPoint3D> addNext3D(DistanceToPoint3D distanceToPoint, Map<Point3D, Collection<Step3D>> network, PriorityQueue<DistanceToPoint3D> unchecked, Map<Point3D, DistanceToPoint3D> checked, Map<Point, Character> map,final Map<Point, String> portalGates) {
        checked.putIfAbsent(distanceToPoint.destination(), distanceToPoint);
        int z = distanceToPoint.destination().getZ();
        if(z <0){
            return Collections.emptyList();
        }
        if(!network.containsKey(distanceToPoint.destination())){
            network.putAll(getNetwork3DLayer(map,portalGates, z));
        }
        List<DistanceToPoint3D> collect = network.get(distanceToPoint.destination())
                .stream()
                .map(w -> new DistanceToPoint3D(distanceToPoint, w, z*z))
                .filter(d -> !distanceToPoint.hasVisited(d.destination()))
                .filter(d -> !checked.containsKey(d.destination()))
                .filter(d -> unchecked.stream().noneMatch(p -> p.destination().equals(d.destination())))
                .collect(Collectors.toList());
        return collect;
    }

    public boolean isUpperCase(Map.Entry<Point, Character> e) {
        return e.getValue().compareTo('A') >= 0 && e.getValue().compareTo('Z') <= 0;
    }


    public void readInput(String inputPath) {

        in = Input.getLines(inputPath);
    }
}
