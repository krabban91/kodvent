package krabban91.kodvent.kodvent.y2019.d03;

import krabban91.kodvent.kodvent.utilities.Distances;
import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class Day3 {
    List<Wire> in;

    public Day3() {
        System.out.println("::: Starting Day 3 :::");
        String inputPath = "y2019/d03/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        List<Map<Point,Integer>> stepsTaken = new ArrayList<>();
        List<Set<Point>> wires = in.stream().map(w -> {
            Map<Point, Integer> map = new HashMap<>();
            stepsTaken.add(map);
            AtomicInteger steps = new AtomicInteger();
            AtomicReference<Point> loc = new AtomicReference<>(new Point(0, 0));
            Set<Point> wireLocations = new HashSet<>();
            for (WirePath path : w.instructions) {
                List<Point> collect = path.pointsInPath.stream()
                        .map(p -> {
                            int i = steps.incrementAndGet();
                            Point point = new Point(loc.get().x + p.x, loc.get().y + p.y);
                            if(!map.containsKey(point)){
                                map.put(point,i);
                            }
                            return point;
                        })
                        .collect(Collectors.toList());
                loc.set(collect.get(collect.size() - 1));
                wireLocations.addAll(collect);
            }
            return wireLocations;
        }).collect(Collectors.toList());
        Set<Point> crossings = new HashSet<>();
        wires.get(0).forEach(p -> {
            if (wires.get(1).contains(p) && !p.equals(new Point(0, 0))) {
                crossings.add(p);

            }
        });
        List<Integer> collect = crossings.stream().map(p -> Distances.manhattan(p, new Point(0, 0)))
                .collect(Collectors.toList());
        OptionalInt min1 = collect.stream().mapToInt(l -> l).min();
        crossings.size();
        // is 7581 wrong
        //157 is too low
        return min1.orElse(-1);
    }

    public long getPart2() {
        List<Map<Point,Integer>> stepsTaken = new ArrayList<>();
        List<Set<Point>> wires = in.stream().map(w -> {
            Map<Point, Integer> map = new HashMap<>();
            stepsTaken.add(map);
            AtomicInteger steps = new AtomicInteger();
            AtomicReference<Point> loc = new AtomicReference<>(new Point(0, 0));
            Set<Point> wireLocations = new HashSet<>();
            for (WirePath path : w.instructions) {
                List<Point> collect = path.pointsInPath.stream()
                        .map(p -> {
                            int i = steps.incrementAndGet();
                            Point point = new Point(loc.get().x + p.x, loc.get().y + p.y);
                            if(!map.containsKey(point)){
                                map.put(point,i);
                            }
                            return point;
                        })
                        .collect(Collectors.toList());
                loc.set(collect.get(collect.size() - 1));
                wireLocations.addAll(collect);
            }
            return wireLocations;
        }).collect(Collectors.toList());
        Set<Point> crossings = new HashSet<>();
        wires.get(0).forEach(p -> {
            if (wires.get(1).contains(p) && !p.equals(new Point(0, 0))) {
                crossings.add(p);

            }
        });
        List<Integer> collect = crossings.stream().mapToInt(p -> stepsTaken.get(0).get(p)+stepsTaken.get(1).get(p))
                .boxed()
                .collect(Collectors.toList());
        OptionalInt min1 = collect.stream().mapToInt(l -> l).min();
        crossings.size();
        // is 7581 wrong
        //157 is too low
        return min1.orElse(-1);
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).stream().map(Wire::new).collect(Collectors.toList());
    }
}
