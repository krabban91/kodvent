package krabban91.kodvent.kodvent.y2019.d02;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day2 {
    List<Integer> in;

    public Day2() {
        System.out.println("::: Starting Day 2 :::");
        String inputPath = "y2019/d02/input.txt";
        readInput(inputPath);
        long part1 = getPart1(this.in, 1, 12);
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1(List<Integer> in, int noun, int verb) {
        List<Integer> result = in;
        result.set(1, noun);
        result.set(2, verb);
        int pointer = 0;
        Integer op = result.get(pointer);
        while (op != 99) {
            if (op == 1) {
                result = add(pointer, new ArrayList<>(result));
                pointer += 4;
            }
            if (op == 2) {
                result = multiply(pointer, new ArrayList<>(result));
                pointer += 4;
            }
            op = result.get(pointer);
        }
        return result.get(0);
    }

    public long getPart2() {
        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                if (getPart1(in, noun, verb) == 19690720) {
                    return 100 * noun + verb;
                }
            }
        }
        return -1;
    }

    public List<Integer> add(int op, List<Integer> input) {
        input.set(input.get(op + 3),
                input.get(input.get(op + 1)) + input.get(input.get(op + 2)));
        return input;
    }

    public List<Integer> multiply(int op, List<Integer> input) {
        input.set(input.get(op + 3),
                input.get(input.get(op + 1)) * input.get(input.get(op + 2)));
        return input;
    }


    public void readInput(String inputPath) {
        in = Stream.of(Input.getSingleLine(inputPath).split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }
}
