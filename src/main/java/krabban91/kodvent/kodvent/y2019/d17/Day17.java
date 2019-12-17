package krabban91.kodvent.kodvent.y2019.d17;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Point3D;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class Day17 {
    List<Long> in;
    List<Point> vectors = Arrays.asList(new Point(0,-1), new Point(1,0), new Point(0,1), new Point(-1,0));

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
        IntCodeComputer computer = new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        computer.setAddress(0,2);
        executor.execute(computer);

        HashMap<Point, Integer> map = new HashMap<>();
        int x = 0;
        int y = 0;
        int maxX = 0;
        Long lastOutput = null;
        boolean mapSearched = false;
        while (!computer.hasHalted() || computer.hasOutput()) {
            lastOutput = computer.pollOutput(1);
            if(lastOutput == null){
                System.out.println(new LogUtils<Integer>().mapToText(map, i -> i == null ? " " : (char) i.intValue() + ""));
                if(!mapSearched){

                    // A -> Left = 65
                    // B -> Forward = 66
                    // C -> Right = 67
                    // commasepararated
                    boolean reachedEnd = false;
                    int currentHeading = 0; // 0 is north, +1 turn right, -1 turn left
                    StringBuilder builder = new StringBuilder();
                    ArrayList<Integer> inputs = new ArrayList<>();
                    Set<Point> hasVisited = new HashSet<>();
                    Point current = map.entrySet().stream().filter(e -> e.getValue() == (int) '^').findAny().map(Map.Entry::getKey).get();
                    while (!reachedEnd){
                        Point forth = vectors.get(getHeading(currentHeading));
                        Point pointForth = new Point(current.x + forth.x, current.y + forth.y);
                        Integer integer = map.get(pointForth);
                        if(integer != null && integer == (int)'#'){
                            inputs.add((int)'B');
                            current = pointForth;
                        } else {
                            int headingLeft = getHeading(currentHeading - 1);
                            Point left = vectors.get(headingLeft);
                            Point pointLeft = new Point(current.x + left.x, current.y + left.y);
                            Integer integerLeft = map.get(pointLeft);
                            if(integerLeft != null && integerLeft == (int)'#'){
                                inputs.add((int)'A');
                                currentHeading = headingLeft;

                            } else {
                                int headingRight = getHeading(currentHeading + 1);
                                Point right = vectors.get(headingRight);
                                Point pointRight = new Point(current.x + right.x, current.y + right.y);
                                Integer integerRight = map.get(pointRight);
                                if(integerRight != null && integerRight == (int)'#'){
                                    inputs.add((int)'C');
                                    currentHeading = headingRight;
                                } else {
                                    reachedEnd = true;
                                }
                            }

                        }
                    }
                    List<Integer> mainRoutine = new ArrayList<>();
                    for (int i = 0; i < inputs.size(); i++) {
                        mainRoutine.add(inputs.get(i));
                        if(i==inputs.size()-1){
                            mainRoutine.add((int)'\n');
                        } else {
                            mainRoutine.add((int)',');
                        }
                    }
                    List<Integer> A = Arrays.asList((int) 'L', (int) '\n');
                    List<Integer> B = Arrays.asList((int) '1', (int) '\n');
                    List<Integer> C = Arrays.asList((int) 'R', (int) '\n');
                    List<Integer> video = Arrays.asList((int) 'y', (int) '\n');
                    mainRoutine.forEach(i->computer.addInput(i.longValue()));
                    A.forEach(i->computer.addInput(i.longValue()));
                    B.forEach(i->computer.addInput(i.longValue()));
                    C.forEach(i->computer.addInput(i.longValue()));
                    video.forEach(i->computer.addInput(i.longValue()));
                    mapSearched = true;
                }

            } else {

                if (lastOutput.intValue() == 10) {
                    x = 0;
                    y++;
                } else {
                    map.put(new Point(x, y), lastOutput.intValue());
                    maxX = Math.max(maxX, x);
                    x++;
                }
            }

        }
        return lastOutput;
    }

    private int getHeading(int heading) {
        return (heading+vectors.size())%vectors.size();
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
