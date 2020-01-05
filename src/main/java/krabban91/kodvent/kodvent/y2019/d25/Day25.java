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
    private static final List<String> wantedItems = Arrays.asList("wreath", "asterisk", "monolith", "astrolabe");
    private static final Deque<String> movingOrder = new LinkedBlockingDeque<>(Arrays.asList("south", "east", "west", "north", "north","north", "west", "west","west", "east", "south", "east", "north", "north"));

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
        StringBuilder previousOutput = new StringBuilder();


        while (!computer.hasHalted() || computer.hasOutput(1000)) {
            if (computer.hasOutput()) {
                previousOutput = new StringBuilder();
                while (computer.hasOutput()) {
                    Long out = computer.pollOutput(2L);
                    previousOutput.append((char) out.intValue());
                }
            }

            System.out.println(previousOutput);
            String s1 = previousOutput.toString();
            if (s1.contains("Analysis complete! You may proceed.")) {
                return s1.split("typing")[1].split("on")[0].trim();
            }
            if (s1.contains("Items here:")) {
                Stream.of(s1.split("Items here:")[1].split("\n\n")[0].split("\n"))
                        .map(e -> e.replace("-", "").trim())
                        .filter(wantedItems::contains)
                        .map(e -> "take " + e+"\n")
                        .forEach(s2 -> s2.chars().boxed().forEachOrdered(computer::addInput));
            }
            if (s1.endsWith("Command?\n")) {
                String s2;
                if(movingOrder.isEmpty()){

                    Scanner scanner = new Scanner(System.in);
                    s2 = scanner.nextLine();
                } else {
                    s2 = movingOrder.pollFirst();
                }
                System.out.println("input: " + s2);
                s2.chars().boxed().forEachOrdered(computer::addInput);
                computer.addInput((long) '\n');
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
