package krabban91.kodvent.kodvent.y2019.d21;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.springframework.stereotype.Component;

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

@Component
public class Day21 {
    List<Long> in;

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
        String input = "NOT B J\n" +
                "NOT C T\n" +
                "OR T J\n" +
                "AND D J\n" +
                "NOT A T\n" +
                "OR T J\n" +
                "WALK\n";
        return inputJumpScript(input);
    }

    private Long inputJumpScript(String input) throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        LinkedBlockingDeque<Long> inputs = new LinkedBlockingDeque<>();
        IntCodeComputer computer = new SpringScriptComputer(in, inputs, new LinkedBlockingDeque<>());
        computer.printProgram();
        executor.execute(computer);
        TimeUnit.SECONDS.sleep(1);
        Long result = null;
        Map<Point, Long> output = new HashMap<>();
        boolean started = false;
        Point p = new Point(0, 0);
        while (!computer.hasHalted() || computer.hasOutput()) {
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
                input.chars().boxed().collect(Collectors.toList()).forEach(i -> computer.addInput(i.longValue()));
                started = true;
                while (!inputs.isEmpty()) {
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

    public long getPart2() throws InterruptedException {
        // E, F, G, H, I
        // 
        // !A || ((!B || !C) && D)
        String input = "NOT B J\n" +
                "NOT C T\n" +
                "OR T J\n" +
                "AND D J\n" +
                "NOT A T\n" +
                "OR T J\n" +
                "RUN\n";
        return inputJumpScript(input);
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}