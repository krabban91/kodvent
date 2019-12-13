package krabban91.kodvent.kodvent.y2019.d13;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class Day13 {
    List<Long> in;

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
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        final LinkedBlockingDeque<Long> outputs = new LinkedBlockingDeque<>();
        IntCodeComputer computer = new IntCodeComputer(in, new LinkedBlockingDeque<>(), outputs);
        executor.execute(computer);
        final Map<Point, Tile> map = new HashMap<>();
        while (!computer.hasHalted() || !outputs.isEmpty()){
            final Long x = computer.pollOutput(2);
            final Long y = computer.pollOutput(2);
            final Long type = computer.pollOutput(2);
            map.put(new Point(x.intValue(),y.intValue()),new Tile(type));
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


    public long getPart2() {

        return -1;
    }

    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(",")).map(Long::new)
                .collect(Collectors.toList());
    }
}
