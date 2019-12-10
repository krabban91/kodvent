package krabban91.kodvent.kodvent.y2019.d10;

import krabban91.kodvent.kodvent.utilities.Distances;
import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day10 {
    List<List<Boolean>> in;

    public Day10() {
        System.out.println("::: Starting Day 10 :::");
        String inputPath = "y2019/d10/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        List<List<AsteroidSlot>> collect = IntStream.range(0, in.size()).mapToObj(y -> IntStream.range(0, in.get(y).size()).mapToObj(x -> new AsteroidSlot(x, y, in.get(y).get(x))).collect(Collectors.toList())).collect(Collectors.toList());
        Map<Point, AsteroidSlot> allSlots = collect.stream().flatMap(List::stream).collect(Collectors.toMap(AsteroidSlot::getPoint, a->a));
        return getStation(allSlots)
                .map(e->e.getValue().size())
                .orElse(-1);
    }

    public Optional<Map.Entry<Point, Set<Double>>> getStation(Map<Point, AsteroidSlot> allSlots) {
        List<AsteroidSlot> asteroidsList = allSlots.values().stream().filter(a -> a.isAsteroid()).collect(Collectors.toList());
        Map<Point, Set<Double>> collect1 = asteroidsList.stream()
                .collect(Collectors.toMap(AsteroidSlot::getPoint, origo -> asteroidsList.stream()
                        .map(asteroid -> satan2(origo.getPoint(), asteroid.getPoint()))
                        .collect(Collectors.toSet())));
        return collect1.entrySet().stream()
                .max(Comparator.comparingInt(e-> e.getValue().size()));
    }

    public long getPart2() {
        List<List<AsteroidSlot>> collect = IntStream.range(0, in.size()).mapToObj(y -> IntStream.range(0, in.get(y).size()).mapToObj(x -> new AsteroidSlot(x, y, in.get(y).get(x))).collect(Collectors.toList())).collect(Collectors.toList());
        Map<Point, AsteroidSlot> allSlots = collect.stream().flatMap(List::stream).collect(Collectors.toMap(AsteroidSlot::getPoint, a->a));
        Point origo = getStation(allSlots).get().getKey();
        int blownUpAsteroids = 0;
        Point latestBlownUp = null;
        double currentAngle = Math.PI/2;
        while(blownUpAsteroids != 200){
            Map<Double, Set<Point>> potentialTargets = getPotentialTargets(origo, allSlots);
            Map.Entry<Double, Point> target = target(origo, potentialTargets, (currentAngle + Math.PI*2)%(Math.PI*2));
            currentAngle = target.getKey();
            latestBlownUp = target.getValue();
            blownUpAsteroids +=1;
            allSlots.get(target.getValue()).obliterate();
        }
        // 405 is too low. 924 is too low
        return latestBlownUp.x*100 + latestBlownUp.y;
    }

    private Map<Double, Set<Point>> getPotentialTargets(Point origo, Map<Point, AsteroidSlot> allSlots) {
        Map<Double, Set<Point>> map = new HashMap<>();
        allSlots.entrySet().stream()
                .filter(e->e.getValue().isAsteroid())
                .forEach(e-> {
            double angle = satan2(origo, e.getKey());
            map.computeIfAbsent(angle, a->new HashSet<>());
            map.get(angle).add(e.getKey());
        });
        return map;
    }
    private Map.Entry<Double, Point> target(Point origo, Map<Double, Set<Point>> potential, double currentAngle) {
        if (currentAngle == 0){
            currentAngle = Math.PI*2;
        }
        double finalCurrentAngle = currentAngle;
        Optional<Map.Entry<Double, Set<Point>>> max = potential.entrySet().stream()
                .filter(e -> e.getKey().compareTo(finalCurrentAngle) < 0)
                .max(Comparator.comparingDouble(e -> e.getKey()));

        Map.Entry<Double, Set<Point>> closestAngleWise = max
                .get();
        Optional<Point> min = closestAngleWise.getValue().stream().min(Comparator.comparingInt(p -> Distances.manhattan(origo, p)));
        return Map.entry(closestAngleWise.getKey(), min.get());
    }


    private double satan2(Point origo, Point point) {
        return (Math.atan2(-1*(point.y - origo.y) , ((double) point.x - origo.x))+Math.PI*2)%(Math.PI*2);
    }


    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).stream().map(s-> s.chars().mapToObj(i-> ((char)i)=='#').collect(Collectors.toList())).collect(Collectors.toList());
    }
}
