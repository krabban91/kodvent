package krabban91.kodvent.kodvent.y2015.d11;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Strings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class Day11 {
    String in;

    public Day11() {
        System.out.println("::: Starting Day 11 :::");
        String inputPath = "y2015/d11/input.txt";
        readInput(inputPath);
        String part1 = getPart1(in);
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        String part2 = getPart2(in);
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public String getPart1(String input) {
        return nextPassword(input);
    }

    public String getPart2(String input) {
        return nextPassword(nextPassword(input));
    }

    public String nextPassword(String input) {
        String output = increment(input);
        while (!isValid(output)) {
            output = increment(output);
        }
        return output;
    }

    public boolean isValid(String input) {
        return rule1(input) && rule2(input) && rule3(input);
    }

    private boolean rule1(String input) {
        return IntStream
                .range(0, input.length() - 2)
                .anyMatch(i -> input.charAt(i) + 1 == input.charAt(i + 1) && input.charAt(i + 1) + 1 == input.charAt(i + 2));
    }

    private boolean rule2(String input) {
        return Stream.of("i", "o", "l")
                .noneMatch(input::contains);
    }

    private boolean rule3(String input) {
        List<Integer> collect = IntStream
                .range(0, input.length() - 1)
                .map(i -> input.charAt(i) == input.charAt(i + 1) ? 1 : 0)
                .mapToObj(i -> i)
                .collect(Collectors.toList());
        if (collect.stream().mapToInt(i -> i).sum() > 2) {
            return true;
        } else if (collect.stream().mapToInt(i -> i).sum() == 2) {

            for (int i = 0; i < collect.size() - 1; i++) {
                if (collect.get(i) == 1 && collect.get(i + 1) == 1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).get(0);
    }

    public String increment(String in) {
        StringBuilder builder = new StringBuilder();
        boolean overflowing = true;
        for (int i = in.length() - 1; i >= 0; i--) {
            char c = in.charAt(i);
            if (overflowing) {
                char next = Strings.ALPHABBET.charAt((Strings.ALPHABBET.lastIndexOf(c) + 1) % Strings.ALPHABBET.length());
                builder.append(next);
                if (next != 'a') {
                    overflowing = false;
                }
            } else {
                builder.append(c);
            }
        }
        return builder.reverse().toString();
    }

}
