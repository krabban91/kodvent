package krabban91.kodvent.kodvent.y2019.d17;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class Day17 {
    List<Long> in;

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

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
