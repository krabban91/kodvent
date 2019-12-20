package krabban91.kodvent.kodvent.y2019.d20;

import krabban91.kodvent.kodvent.utilities.Grid;
import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.utilities.search.Graph;
import krabban91.kodvent.kodvent.y2018.d15.DistanceToPoint;
import krabban91.kodvent.kodvent.y2018.d15.Step;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
                .map(w -> new DistanceToPoint(distanceToPoint, w,0))
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

    public boolean isUpperCase(Map.Entry<Point, Character> e) {
        return e.getValue().compareTo('A') >= 0 && e.getValue().compareTo('Z') <= 0;
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {

        in = Input.getLines(inputPath);
    }
}
