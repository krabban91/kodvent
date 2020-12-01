package krabban91.kodvent.kodvent.y2019.d13;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day13 {
    List<Long> in;
    private boolean debug = true;

    public Day13() throws InterruptedException {
        System.out.println("::: Starting Day 13 :::");
        String inputPath = "y2019/d13/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        final LinkedBlockingDeque<Long> outputs = new LinkedBlockingDeque<>();
        IntCodeComputer computer = new IntCodeComputer(in, new LinkedBlockingDeque<>(), outputs);
        executor.execute(computer);
        final Map<Point, Tile> map = new HashMap<>();
        while (!computer.hasHalted() || !outputs.isEmpty()) {
            final Long x = computer.pollOutput(2);
            final Long y = computer.pollOutput(2);
            final Long type = computer.pollOutput(2);
            map.put(new Point(x.intValue(), y.intValue()), new Tile(type));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1L, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return map.values().stream()
                .filter(Tile::isBlock)
                .count();
    }


    public long getPart2() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        IntCodeComputer computer = new IntCodeComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        computer.setAddress(0, 2);
        executor.execute(computer);
        final Map<Point, Tile> map = new HashMap<>();
        Long score = null;
        boolean started = false;
        while (!computer.hasHalted() || computer.hasOutput()) {
            final Long x = computer.pollOutput(2);
            final Long y = computer.pollOutput(2);
            final Long type = computer.pollOutput(2);
            if (x == -1 && y == 0) {
                score = type;
                if (!started) {
                    started = true;
                    computer.addInput(0);
                }
            } else {
                final Point key = new Point(x.intValue(), y.intValue());
                final Tile tile = new Tile(type);
                map.put(key, tile);
                if (map.size() == 740 && !computer.hasOutput()) {
                    if (debug) {
                        System.out.println("Score: " + score);
                        System.out.println(new LogUtils<Tile>().mapToText(map, t -> t == null ? " " : t.toString()));
                    }
                    Point ball = map.entrySet().stream()
                            .filter(e -> e.getValue().isBall())
                            .findFirst()
                            .map(Map.Entry::getKey).get();
                    Point paddle = map.entrySet().stream()
                            .filter(e -> e.getValue().isPaddle())
                            .findFirst()
                            .map(Map.Entry::getKey).get();
                    computer.addInput(Integer.compare(ball.x, paddle.x));
                }
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1L, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return score;
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
