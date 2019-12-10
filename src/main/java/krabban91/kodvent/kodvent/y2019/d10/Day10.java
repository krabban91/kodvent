package krabban91.kodvent.kodvent.y2019.d10;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Map;
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

        List<AsteroidSlot> allSlots = collect.stream().flatMap(List::stream).collect(Collectors.toList());
        List<AsteroidSlot> asteroidsList = allSlots.stream().filter(a -> a.isAsteroid()).collect(Collectors.toList());
        Map<Point, Set<Double>> collect1 = asteroidsList.stream()
                .collect(Collectors.toMap(AsteroidSlot::getPoint, a -> asteroidsList.stream()
                        .map(b -> angle(a, b))
                        .collect(Collectors.toSet())));
        return  collect1.values().stream()
                .mapToInt(Set::size)
                .max()
                .orElse(-1);
    }

    public long getPart2() {
        return -1;
    }

    private double angle(Point a, Point b) {
        return Math.atan2((a.y - b.y) , ((double) a.x - b.x));
    }
    private double angle(AsteroidSlot a, AsteroidSlot b) {
        return Math.atan2((a.getPoint().y - b.getPoint().y) , ((double) a.getPoint().x - b.getPoint().x));
    }


    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).stream().map(s-> s.chars().mapToObj(i-> ((char)i)=='#').collect(Collectors.toList())).collect(Collectors.toList());
    }
}
