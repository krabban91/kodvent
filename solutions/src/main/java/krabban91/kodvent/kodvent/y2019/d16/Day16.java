package krabban91.kodvent.kodvent.y2019.d16;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 {
    String in;
    List<Integer> basePattern = Arrays.asList(0, 1, 0, -1);

    public Day16() {
        System.out.println("::: Starting Day 16 :::");
        String inputPath = "y2019/d16/input.txt";
        readInput(inputPath);
        String part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        String part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public String getPart1() {
        List<Integer> input = in.chars().mapToObj(c -> Integer.parseInt((char) c + "")).collect(Collectors.toList());

        for (int i = 0; i < 100; i++) {
            input = FFT(input, basePattern);
        }
        return input.subList(0, 8).stream().reduce("", (a, b) -> a + "" + b, (s1, s2) -> s1 + s2);
    }

    public String getPart2() {

        final List<Integer> initialInput = Strings.repeated(in, 10000).chars().mapToObj(c -> Integer.parseInt((char) c + "")).collect(Collectors.toList());
        final List<Integer> offSetList = initialInput.subList(0, 7);
        final int offSet = Integer.parseInt(offSetList.stream().reduce("", (a, b) -> a + "" + b, (s1, s2) -> s1 + s2));

        List<Integer> input = initialInput;
        for (int i = 0; i < 100; i++) {
            if (basePattern.get(1) == 1 && offSet > input.size() / 2) {
                input = FFT2(input, offSet);
            } else {
                input = FFT(input, basePattern);
            }
        }
        return input.subList(offSet, offSet + 8).stream().reduce("", (a, b) -> a + "" + b, (s1, s2) -> s1 + s2);
    }

    private List<Integer> FFT(List<Integer> input, List<Integer> basePattern) {
        List<Integer> output = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            output.add(calculate(input, i, basePattern));
        }
        return output;
    }

    private List<Integer> FFT2(List<Integer> input, int offset) {
        List<Integer> output = new ArrayList<>(input);
        for (int i = input.size() - 2; i >= offset; i--) {
            output.set(i, (output.get(i) + output.get(i + 1)) % 10);
        }
        return output;
    }

    public int calculate(List<Integer> input, int inputIndex, List<Integer> basePattern) {
        return Math.abs(IntStream.range(0, input.size()).map(i -> input.get(i) * getPatternValue(inputIndex, i, basePattern)).sum()) % 10;
    }

    public int getPatternValue(int inputIndex, int patternIndex, List<Integer> basePattern) {
        return basePattern.get(getBasePatternIndex(inputIndex, patternIndex, basePattern));
    }

    public int getBasePatternIndex(int inputIndex, int patternIndex, List<Integer> basePattern) {
        return (((patternIndex + 1) / (inputIndex + 1)) + basePattern.size()) % basePattern.size();
    }

    public void readInput(String inputPath) {
        in = Input.getSingleLine(inputPath);
    }
}
