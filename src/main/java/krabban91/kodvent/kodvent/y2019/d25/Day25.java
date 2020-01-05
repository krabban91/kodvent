package krabban91.kodvent.kodvent.y2019.d25;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.AsciiComputer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day25 {

    List<Long> in;


    public Day25() throws InterruptedException {
        System.out.println("::: Starting Day 25 :::");
        String inputPath = "y2019/d25/input.txt";
        readInput(inputPath);
        String part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
    }

    public String getPart1() throws InterruptedException {
        List<String> wantedItems = Arrays.asList("wreath", "asterisk", "monolith", "astrolabe");
        Deque<String> movingOrder = new LinkedBlockingDeque<>(Arrays.asList("south", "east", "west", "north", "north", "north", "west", "west", "west", "east", "south", "east", "north", "north"));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        AsciiComputer computer = new AsciiComputer(in);
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
                        .map(e -> "take " + e)
                        .forEach(computer::inputAscii);
            }
            if (s1.endsWith("Command?\n")) {
                String input;
                if (movingOrder.isEmpty()) {

                    Scanner scanner = new Scanner(System.in);
                    input = scanner.nextLine();
                } else {
                    input = movingOrder.pollFirst();
                }
                System.out.println("input: " + input);
                computer.inputAscii(input);
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

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
