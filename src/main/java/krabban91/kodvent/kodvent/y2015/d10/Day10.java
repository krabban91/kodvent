package krabban91.kodvent.kodvent.y2015.d10;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class Day10 {
    String in;

    public int getPart1() {
        return lookAndSay(in, 40).length();
    }

    public long getPart2() {
        return lookAndSay(in, 50).length();
    }

    private String lookAndSay(String in, int i) {
        String lookAt = in;
        while (i > 0) {
            lookAt = lookAndSay(lookAt);
            i--;
        }
        return lookAt;
    }

    private String lookAndSay(String lookAt) {
        StringBuilder builder = new StringBuilder();
        Character current = null;

        int count = 1;
        for (int i = 0; i < lookAt.length(); i++) {
            if (current == null) {
                current = lookAt.charAt(i);
                count = 1;
            } else if (current != lookAt.charAt(i)) {
                builder.append(count);
                builder.append(current);
                current = lookAt.charAt(i);
                count = 1;
            } else {
                count++;
            }
        }
        builder.append(count);
        builder.append(current);
        return builder.toString();
    }

    public void readInput(String inputPath) {
        in = Input.getSingleLine(inputPath);
    }

    public Day10() {
        System.out.println("::: Starting Day 10 :::");
        String inputPath = "y2015/d10/input.txt";
        readInput(inputPath);
        int part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
