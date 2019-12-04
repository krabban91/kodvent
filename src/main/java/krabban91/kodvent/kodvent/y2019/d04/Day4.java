package krabban91.kodvent.kodvent.y2019.d04;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class Day4 {
    String in;

    public Day4() {
        System.out.println("::: Starting Day 4 :::");
        String inputPath = "y2019/d04/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        String[] split = in.split("-");
        int lower = Integer.parseInt(split[0]);
        int upper = Integer.parseInt(split[1]);
        return IntStream.rangeClosed(lower, upper)
                .filter(this::alwaysIncreasing)
                .filter(this::hasTwin)
                .count();
    }

    public long getPart2() {
        String[] split = in.split("-");
        int lower = Integer.parseInt(split[0]);
        int upper = Integer.parseInt(split[1]);
        return IntStream.rangeClosed(lower, upper)
                .filter(this::alwaysIncreasing)
                .filter(this::hasStrictTwin)
                .count();
    }

    public boolean hasStrictTwin(int i) {
        String s = i + "";
        int size = 1;
        char c = s.charAt(0);
        for (int j = 1; j < s.length(); j++) {
            if (s.charAt(j) == c) {
                size++;
            } else {
                if (size == 2) {
                    return true;
                }
                size = 1;
            }
            c = s.charAt(j);

        }
        return size == 2;
    }

    public boolean hasTwin(int i) {
        String s = i + "";
        char c = s.charAt(0);
        for (int j = 1; j < s.length(); j++) {
            if (s.charAt(j) == c) {
                return true;
            }
            c = s.charAt(j);
        }
        return false;
    }

    public boolean alwaysIncreasing(int i) {
        String s = i + "";
        int c = Integer.parseInt(s.charAt(0) + "");
        for (int j = 1; j < s.length(); j++) {

            if (Integer.parseInt(s.charAt(j) + "") < c) {
                return false;
            }
            c = Integer.parseInt(s.charAt(j) + "");
        }
        return true;
    }

    public void readInput(String inputPath) {

        in = Input.getSingleLine(inputPath);
    }
}
