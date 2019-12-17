package krabban91.kodvent.kodvent.y2019.d16;

import krabban91.kodvent.kodvent.utilities.Input;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
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

        final Map<Integer, List<Integer>> patterns = generatePatterns(basePattern, input.size());
        for (int i = 0; i < 100; i++) {
            input = FFT(input, patterns);
        }
        return input.subList(0,8).stream().reduce("", (a,b) -> a+""+b, (s1,s2)-> s1+s2);
    }

    public String getPart2() {
        final List<Integer> initialInput = in.chars().mapToObj(c -> Integer.parseInt((char) c + "")).collect(Collectors.toList());
        List<Integer> input = repeatTerms(initialInput,1000);
        final List<Integer> offSetList = initialInput.subList(0, 7);
        final int offSet = Integer.parseInt(offSetList.stream().reduce("", (a,b) -> a+""+b, (s1,s2)-> s1+s2));

        final Map<Integer, List<Integer>> patterns = generatePatterns(basePattern, input.size());
        for (int i = 0; i < 100; i++) {
            input = FFT(input, patterns);
        }
        return input.subList(offSet,offSet+8).stream().reduce("", (a,b) -> a+""+b, (s1,s2)-> s1+s2);
    }

    public List<Integer> FFT(List<Integer> input, Map<Integer,List<Integer>> patterns) {
        List<Integer> output = new ArrayList<>();
        for (int i = 0; i <input.size(); i++) {
            output.add(calculate(input, patterns.get(i)));
        }
        return output;
    }

    public Map<Integer,List<Integer>> generatePatterns(List<Integer> basePattern, int inputSize){
        return IntStream.range(0,inputSize)
                .boxed()
                .collect(Collectors.toMap(
                        i->i,
                        i->patternForIndex(basePattern,i)));
    }

    public List<Integer> patternForIndex(List<Integer> basePattern, int index) {
        List<Integer> result = repeatTerms(basePattern, index+1);
        return repeatAndShiftPattern(result);
    }

    public List<Integer> repeatTerms(List<Integer> terms, int times) {
        return IntStream.range(0,terms.size())
                .mapToObj(i-> IntStream.range(0,times)
                        .mapToObj(t-> terms.get(i))
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Integer> repeatAndShiftPattern(List<Integer> basePattern) {
        return IntStream.rangeClosed(0, in.length()+1)
                .mapToObj(i -> basePattern.get(i % basePattern.size()))
                .collect(Collectors.toList()).subList(1,in.length()+1);
    }

    public int calculate(List<Integer> input, List<Integer> pattern){
        return Math.abs(IntStream.range(0,input.size()).map(i-> input.get(i)*pattern.get(i)).sum())%10;
    }

    public void readInput(String inputPath) {
        in = Input.getSingleLine(inputPath);
    }
}
