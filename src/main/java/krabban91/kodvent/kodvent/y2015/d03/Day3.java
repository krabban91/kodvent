package krabban91.kodvent.kodvent.y2015.d03;

import krabban91.kodvent.kodvent.utilities.Input;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Day3 {
    private List<Point> deltas;

    public long getPart1() {
        Set<Point> houses = new HashSet<>();
        Point location = new Point(0, 0);
        deliverPackage(houses, location);
        deltas.forEach(p -> {
            location.translate(p.x, p.y);
            deliverPackage(houses, location);
        });
        return houses.size();

    }

    public int getPart2() {
        AtomicBoolean isRobot = new AtomicBoolean(false);
        Set<Point> houses = new HashSet<>();
        Point santa = new Point(0, 0);
        Point robotSanta = new Point(0, 0);
        deliverPackage(houses, santa);
        deliverPackage(houses, robotSanta);
        deltas.forEach(p -> {
            if (isRobot.get()) {
                robotSanta.translate(p.x, p.y);
                deliverPackage(houses, robotSanta);
            } else {
                santa.translate(p.x, p.y);
                deliverPackage(houses, santa);
            }
            isRobot.set(!isRobot.get());
        });
        return houses.size();
    }

    private void deliverPackage(Set<Point> houses, Point location) {
        houses.add((Point) location.clone());
    }

    public void readInput(String inputPath) {
        deltas = Input.getSingleLine(inputPath).chars().mapToObj(this::mapToDelta).collect(Collectors.toList());
    }

    private Point mapToDelta(int c) {
        switch (c) {
            case (int) '^':
                return new Point(0, -1);
            case (int) '>':
                return new Point(1, 0);
            case (int) 'v':
                return new Point(0, 1);
            case (int) '<':
                return new Point(-1, 0);
        }
        return null;
    }

    public Day3() {
        System.out.println("::: Starting Day 3 :::");
        String inputPath = "y2015/d03/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
