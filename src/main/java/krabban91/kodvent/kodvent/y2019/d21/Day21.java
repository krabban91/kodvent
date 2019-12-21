package krabban91.kodvent.kodvent.y2019.d21;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Point3D;
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
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        IntCodeComputer computer = new SpringScriptComputer(in, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        computer.setAddress(0, 2);
        printProgram(in);
        executor.execute(computer);
        Map<Point, Long> output = new HashMap<>();

        String jump_if_next_is_hole = "NOT A J\n";
        while(computer.hasHalted() || computer.hasOutput()){
            output.clear();
            Point p = new Point(0,0);
            while (computer.hasOutput()){
                Long out = computer.pollOutput(2L);
                if(out == (long)'\n'){
                    p.translate(-p.x,1);
 
                } else {
                    output.put(new Point(p),out);
                    p.translate(1,0);
                }
            }
            System.out.println(new LogUtils<Long>().mapToText(output, v-> v== null? " ": (char)v.intValue() + ""));
            
        }
        try {
            executor.awaitTermination(2L, TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    private void printProgram(List<Long> program) {
        Map<Point, Long> output = new HashMap<>();
        Point point = new Point(0, 0);
        program.forEach(l->{
            if(l == (long)'\n'){
                point.translate(-point.x,1);
            } else {
                output.put(new Point(point),l);
                point.translate(1,0);
            }
        });
        System.out.println(new LogUtils<Long>().mapToText(output, v-> v== null? " ": (char)v.intValue() + ""));
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
