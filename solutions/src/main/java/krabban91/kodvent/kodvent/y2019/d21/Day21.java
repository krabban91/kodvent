package krabban91.kodvent.kodvent.y2019.d21;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day21 {
    List<Long> in;
    private boolean verbose = false;

    public Day21() throws InterruptedException {
        System.out.println("::: Starting Day 21 :::");
        String inputPath = "y2019/d21/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() throws InterruptedException {
        // !A || ((!B || !C) && D)
        List<String> rules = Arrays.asList("NOT B J", "NOT C T", "OR T J",
                "AND D J", "NOT A T", "OR T J", "WALK");
        return scanHullForHoles(rules);
    }

    public long getPart2() throws InterruptedException {
        // !A || ((!B || !C) && (D && (E || (!E && H))))
        List<String> rules = Arrays.asList(
                "NOT B J", "NOT C T", "OR T J", "NOT E T", "AND H T",
                "OR E T", "AND D T", "AND T J", "NOT A T", "OR T J",
                "RUN");
        return scanHullForHoles(rules);
    }

    private Long scanHullForHoles(List<String> input) throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        SpringScriptComputer computer = new SpringScriptComputer(in);
        if (verbose) {
            computer.printProgram();
        }
        executor.execute(computer);
        TimeUnit.SECONDS.sleep(1);
        Long result = null;
        Map<Point, Long> output = new HashMap<>();
        boolean started = false;
        Point p = new Point(0, 0);
        while (!computer.hasHalted() || computer.hasOutput(1000)) {
            while (computer.hasOutput()) {
                Long out = computer.pollOutput(2L);
                if (out == (long) '\n') {
                    p.translate(-p.x, 1);
                } else if (out.compareTo(256L) > 0) {
                    result = out;
                } else {
                    output.put(new Point(p), out);
                    p.translate(1, 0);
                }
            }

            if (!started) {
                System.out.println(new LogUtils<Long>().mapToText(output, v -> v == null ? " " : (char) v.intValue() + ""));
                input.forEach(computer::addSpringScriptInstructions);
                computer.activateInstructions();
                started = true;
                while (!computer.hasReadyInput()) {
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }
        System.out.println(new LogUtils<Long>().mapToText(output, v -> v == null ? " " : (char) v.intValue() + ""));
        try {
            executor.awaitTermination(2L, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
