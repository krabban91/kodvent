package krabban91.kodvent.kodvent.y2019.d03;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.List;
import java.util.stream.Collectors;

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
        CircuitBoard circuitBoard = new CircuitBoard(in.get(0), in.get(1));
        return circuitBoard.closestIntersection()
                .map(circuitBoard::distanceFromCenter)
                .orElse(-1);
    }

    public long getPart2() {
        CircuitBoard circuitBoard = new CircuitBoard(in.get(0), in.get(1));
        return circuitBoard.intersectionWithLeastWiring()
                .map(circuitBoard::combinedWireLength)
                .orElse(-1);
    }


    public void readInput(String inputPath) {
        in = Input.getLines(inputPath)
                .stream()
                .map(Wire::new)
                .collect(Collectors.toList());
    }
}
