package krabban91.kodvent.kodvent.y2016.d02;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Day2 {

    List<List<Integer>> instructions;
    private Map<Character, Point> movementVectors = Map.of(
            'U', new Point(0, -1),
            'R', new Point(1, 0),
            'D', new Point(0, 1),
            'L', new Point(-1, 0));

    private char[][] keypad1 = {
            {'1', '2', '3'},
            {'4', '5', '6'},
            {'7', '8', '9'}
    };

    private char[][] keypad2 = {
            {'.', '.', '1', '.', '.'},
            {'.', '2', '3', '4', '.'},
            {'5', '6', '7', '8', '9'},
            {'.', 'A', 'B', 'C', '.'},
            {'.', '.', 'D', '.', '.'}
    };

    public Day2() {
        System.out.println("::: Starting Day 2 :::");
        String inputPath = "y2016/d02/input.txt";
        readInput(inputPath);
        String part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        String part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }


    public String getPart1() {
        Point location = new Point(1, 1);
        return traverseKeyPad(location, keypad1);
    }

    private String traverseKeyPad(Point start, char[][] keypad) {
        StringBuilder output = new StringBuilder();
        for (List<Integer> l : instructions) {
            for (Integer move : l) {
                start = move(start, getVector(move), keypad);
            }
            output.append(getValue(start, keypad));
        }
        return output.toString();
    }

    public String getPart2() {
        Point location = new Point(0, 2);
        return traverseKeyPad(location, keypad2);

    }

    private char getValue(Point point, char[][] map) {
        return map[point.y][point.x];
    }

    private Point move(Point from, Point vector, char[][] map) {
        int newX = Math.min(Math.max(from.x + vector.x, 0), map.length-1);
        int newY = Math.min(Math.max(from.y + vector.y, 0), map[0].length-1);
        if(map[newY][newX]=='.'){
            return from;
        }
        return new Point(newX, newY);
    }

    private Point getVector(Integer direction) {
        return movementVectors.get((char) direction.intValue());
    }

    public void readInput(String inputPath) {
        instructions = Input.getLines(inputPath)
                .stream()
                .map(e -> e.chars()
                        .boxed()
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
