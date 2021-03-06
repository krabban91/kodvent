package krabban91.kodvent.kodvent.y2019.d17;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.y2019.shared.AsciiComputer;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day17 {
    List<Long> in;
    List<Point> vectors = Arrays.asList(new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0));

    public Day17() throws InterruptedException {
        System.out.println("::: Starting Day 17 :::");
        String inputPath = "y2019/d17/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        IntCodeComputer computer = new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        executor.execute(computer);

        HashMap<Point, Integer> map = new HashMap<>();
        int x = 0;
        int y = 0;
        int maxX = 0;
        while (!computer.hasHalted() || computer.hasOutput()) {
            Long aLong = computer.pollOutput(1);
            if (aLong.intValue() == 10) {
                x = 0;
                y++;
            } else {
                map.put(new Point(x, y), aLong.intValue());
                maxX = Math.max(maxX, x);
                x++;
            }
        }
        int maxY = y - 1;
        System.out.println(new LogUtils<Integer>().mapToText(map, i -> i == null ? " " : (char) i.intValue() + ""));
        int finalMaxX = maxX;
        try {
            executor.awaitTermination(2L, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return IntStream.range(1, maxY - 1).mapToObj(itY ->
                IntStream.range(1, finalMaxX - 1)
                        .filter(itX -> map.get(new Point(itX, itY)) == 35)
                        .filter(itX -> Stream.of(
                                map.get(new Point(itX - 1, itY)),
                                map.get(new Point(itX, itY - 1)),
                                map.get(new Point(itX + 1, itY)),
                                map.get(new Point(itX, itY + 1)))
                                .filter(i -> i == 35)
                                .count() == 4
                        )
                        .mapToObj(itX -> itX * itY)
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .mapToInt(i -> i)
                .sum();
    }

    public long getPart2() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        AsciiComputer computer = new AsciiComputer(in);
        computer.setAddress(0, 2);
        executor.execute(computer);

        HashMap<Point, Integer> map = new HashMap<>();
        int x = 0;
        int y = 0;
        long largestOut = 0;
        boolean mapSearched = false;
        while (!computer.hasHalted() || computer.hasOutput()) {
            Long lastOutput = computer.pollOutput(1);

            if (lastOutput == null) {
                System.out.println(new LogUtils<Integer>().mapToText(map, i -> i == null ? "  " : (char) i.intValue() + " "));
                if (!mapSearched) {
                    boolean reachedEnd = false;
                    int currentHeading = 0; // 0 is north, +1 turn right, -1 turn left
                    ArrayList<Integer> inputs = new ArrayList<>();
                    Point current = map.entrySet().stream().filter(e -> e.getValue() == (int) '^').findAny().map(Map.Entry::getKey).get();
                    int range = 0;
                    while (!reachedEnd) {
                        Point forth = vectors.get(getHeading(currentHeading));
                        Point pointForth = new Point(current.x + forth.x, current.y + forth.y);
                        Integer integer = map.get(pointForth);
                        if (integer != null && integer == (int) '#') {
                            if (range < 9) {
                                range++;

                            } else {
                                inputs.add((int) '9');
                                range = 1;
                            }
                            current = pointForth;
                        } else {
                            int headingLeft = getHeading(currentHeading - 1);
                            Point left = vectors.get(headingLeft);
                            Point pointLeft = new Point(current.x + left.x, current.y + left.y);
                            Integer integerLeft = map.get(pointLeft);
                            if (integerLeft != null && integerLeft == (int) '#') {
                                if (range != 0) {
                                    inputs.add((int) (range + "").charAt(0));
                                    range = 0;
                                }
                                inputs.add((int) 'L');
                                currentHeading = headingLeft;

                            } else {
                                int headingRight = getHeading(currentHeading + 1);
                                Point right = vectors.get(headingRight);
                                Point pointRight = new Point(current.x + right.x, current.y + right.y);
                                Integer integerRight = map.get(pointRight);
                                if (integerRight != null && integerRight == (int) '#') {
                                    if (range != 0) {
                                        inputs.add((int) (range + "").charAt(0));
                                        range = 0;
                                    }
                                    inputs.add((int) 'R');
                                    currentHeading = headingRight;
                                } else {
                                    reachedEnd = true;
                                }
                            }
                        }
                    }
                    StringBuilder b = new StringBuilder();
                    inputs.forEach(i -> b.append((char) i.intValue()));
                    System.out.println(b.toString());

                    // TODO: perform zip algorithm.
                    List<Integer> mainRoutine = new ArrayList<>();
                    for (int i = 0; i < inputs.size(); i++) {
                        mainRoutine.add(inputs.get(i));
                        if (i == inputs.size() - 1) {
                            mainRoutine.add((int) '\n');
                        } else {
                            mainRoutine.add((int) ',');
                        }
                    }
                    String main = "A,B,A,B,A,C,B,C,A,C";
                    computer.inputAscii(main);
                    String A = "L,6,R,9,3,L,6";
                    computer.inputAscii(A);
                    String B = "R,9,3,L,9,1,L,4,L,6";
                    computer.inputAscii(B);
                    String C = "L,9,1,L,9,1,L,4,L,6";
                    computer.inputAscii(C);
                    String video = "n";
                    computer.inputAscii(video);
                    mapSearched = true;
                }
            } else {
                largestOut = Math.max(largestOut, lastOutput);
                if (lastOutput.intValue() == 10) {
                    x = 0;
                    y++;
                } else {
                    map.put(new Point(x, y), lastOutput.intValue());
                    x++;
                }
            }

        }
        System.out.println(new LogUtils<Integer>().mapToText(map, i -> i == null ? "  " : (char) i.intValue() + " "));
        try {
            executor.awaitTermination(2L, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return largestOut;
    }

    private int getHeading(int heading) {
        return (heading + vectors.size()) % vectors.size();
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
