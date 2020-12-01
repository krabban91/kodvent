package krabban91.kodvent.kodvent.y2019.d03;


import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Wire {

    List<WirePath> instructions;
    Map<Point, Integer> wireLengthToPoin = new HashMap<>();
    Set<Point> points = new HashSet<>();

    public Wire(String s) {
        instructions = Stream.of(s.split(",")).map(WirePath::new).collect(Collectors.toList());
        trace();
    }

    public void trace() {
        AtomicInteger steps = new AtomicInteger();
        AtomicReference<Point> loc = new AtomicReference<>(new Point(0, 0));
        for (WirePath path : this.instructions) {
            List<Point> collect = path.pointsInPath.stream()
                    .map(p -> {
                        int i = steps.incrementAndGet();
                        Point point = new Point(loc.get().x + p.x, loc.get().y + p.y);
                        if(!wireLengthToPoin.containsKey(point)){
                            wireLengthToPoin.put(point,i);
                        }
                        return point;
                    })
                    .collect(Collectors.toList());
            loc.set(collect.get(collect.size() - 1));
            points.addAll(collect);
        }
    }
    public boolean intersectsWithPoint(Point p){
        return points.contains(p);
    }

    public Optional<Integer> wireLengthTo(Point p){
        return wireLengthToPoin.entrySet().stream()
                .filter(e-> e.getKey().equals(p))
                .findFirst()
                .map(Map.Entry::getValue);
    }
}
