package krabban91.kodvent.kodvent.y2015.d14;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Day14 {
    List<String> in;

    public Day14() {
        System.out.println("::: Starting Day 14 :::");
        String inputPath = "y2015/d14/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        List<Reindeer> reindeers = performRace();
        return reindeers.stream().mapToLong(Reindeer::getDistance).max().orElse(0);
    }

    public long getPart2() {
        List<Reindeer> reindeers = performRace();
        return reindeers.stream().mapToLong(Reindeer::getScore).max().orElse(0);
    }

    private List<Reindeer> performRace() {
        List<Reindeer> reindeers = in.stream().map(Reindeer::new).collect(Collectors.toList());
        int second = 0;
        while (second < 2503){
            reindeers.forEach(Reindeer::move);
            reindeers.stream().max(Comparator.comparingLong(Reindeer::getDistance)).get().score();
            second++;
        }
        return reindeers;
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath);
    }
}
