package krabban91.kodvent.kodvent.y2019.d03;

import krabban91.kodvent.kodvent.utilities.Distances;

import java.awt.*;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CircuitBoard {
    private final Wire a;
    private final Wire b;

    public CircuitBoard(Wire a, Wire b) {
        this.a = a;
        this.b = b;
    }

    public Optional<Point> closestIntersection() {
        Set<Point> intersections = intersections();
        return intersections.stream()
                .min(Comparator.comparingInt(p -> Distances.manhattan(p, new Point(0, 0))));
    }

    public Optional<Point> intersectionWithLeastWiring() {
        Set<Point> intersections = intersections();
        return intersections.stream()
                .min(Comparator.comparingInt(this::combinedWireLength));
    }

    public Integer distanceFromCenter(Point p) {
        return Distances.manhattan(p, new Point(0, 0));
    }

    public int combinedWireLength(Point p) {
        return this.a.wireLengthTo(p).orElse(-1) + this.b.wireLengthTo(p).orElse(-1);
    }

    private Set<Point> intersections() {
        return a.points.stream()
                .filter(p -> !p.equals(new Point(0, 0)))
                .filter(b::intersectsWithPoint)
                .collect(Collectors.toSet());
    }
}
