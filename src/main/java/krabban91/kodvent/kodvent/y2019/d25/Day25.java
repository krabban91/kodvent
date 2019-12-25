package krabban91.kodvent.kodvent.y2019.d25;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day25 {
    private static final Point VEC_NORTH = new Point(0, -1);
    private static final Point VEC_EAST = new Point(1, 0);
    private static final Point VEC_SOUTH = new Point(0, 1);
    private static final Point VEC_WEST = new Point(-1, 0);
    private static final Map<String, Point> VECTORS = Map.of("north", VEC_NORTH, "south", VEC_SOUTH, "west", VEC_WEST, "east", VEC_EAST);
    private static final Map<Point, String> COMMANDS = Map.of(VEC_NORTH, "north\n", VEC_SOUTH, "south\n", VEC_WEST, "west\n", VEC_EAST, "east\n");
    private static final Map<Point, Point> VECTORS_BACK = Map.of(
            VEC_NORTH, VEC_SOUTH,
            VEC_SOUTH, VEC_NORTH,
            VEC_WEST, VEC_EAST,
            VEC_EAST, VEC_WEST);
    List<Long> in;


    public Day25() throws InterruptedException {
        System.out.println("::: Starting Day 25 :::");
        String inputPath = "y2019/d25/input.txt";
        readInput(inputPath);
        String part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public String getPart1() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        LinkedBlockingDeque<Long> inputs = new LinkedBlockingDeque<>();
        IntCodeComputer computer = new IntCodeComputer(in, inputs, new LinkedBlockingDeque<>());
        executor.execute(computer);

        int x = 0;
        int y = 0;
        Map<Point, Long> output = new HashMap<>();
        boolean started = false;
        Point p = new Point(0, 0);
        StringBuilder previousOutput = new StringBuilder();

        HashMap<Point, Integer> map = new HashMap<>();
        Point location = new Point(0, 0);
        Deque<Point> pathBackToStart = new LinkedBlockingDeque<>();

        while (!computer.hasHalted() || computer.hasOutput(1000)) {
            if (computer.hasOutput()) {
                previousOutput = new StringBuilder();
                while (computer.hasOutput()) {
                    Long out = computer.pollOutput(2L);
                    previousOutput.append((char) out.intValue());

                    if (out == (long) '\n') {
                        p.translate(-p.x, 1);
                    } else {
                        output.put(new Point(p), out);
                        p.translate(1, 0);


                    }
                }
            }
            String s = new LogUtils<Long>().mapToText(output, v -> v == null ? " " : (char) v.intValue() + "");
            System.out.println(previousOutput);
            String s1 = previousOutput.toString();
            if(s1.contains("Analysis complete! You may proceed.")){
                return s1.split("typing ")[1].split(" on")[0];
            }
            if (s1.endsWith("Command?\n")) {
                Scanner scanner = new Scanner(System.in);
                String s2 = scanner.nextLine();
                System.out.print("input: " + s2);
                s2.chars().boxed().forEachOrdered(computer::addInput);
                computer.addInput((long)'\n');
                /*if (s1.contains("Doors here lead")) {
                    List<Point> options = Arrays.stream(s1.split("\n")).filter(r -> r.startsWith("-"))
                            .map(r -> r.split(" ")[1])
                            .map(VECTORS::get)
                            .collect(Collectors.toList());
                    List<Point> notBeen = options.stream().map(r -> new Point(r.x + location.x, r.y + location.y)).filter(r -> !map.containsKey(r)).collect(Collectors.toList());
                    if (notBeen.isEmpty()) {
                        if (pathBackToStart.isEmpty()) {
                            //back at start
                            break;
                        }
                        Point point = pathBackToStart.pollLast();
                        location.translate(point.x, point.y);
                        String s2 = COMMANDS.get(point);
                        System.out.print("input: " + s2);
                        s2.chars().boxed().forEachOrdered(computer::addInput);

                    } else {
                        Point point = notBeen.get(0);
                        location.translate(point.x, point.y);
                        pathBackToStart.addLast(VECTORS_BACK.get(point));
                        String s2 = COMMANDS.get(point);
                        System.out.print("input: " + s2);
                        s2.chars().boxed().forEachOrdered(computer::addInput);

                    }
                    System.out.println(previousOutput);
                }*/
            }
        }
        try {
            executor.awaitTermination(2L, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "-1L";
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
