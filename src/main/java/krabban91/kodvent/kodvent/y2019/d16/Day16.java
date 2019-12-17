package krabban91.kodvent.kodvent.y2019.d16;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day16 {
    String in;

    public Day16() {
        System.out.println("::: Starting Day 16 :::");
        String inputPath = "y2019/d16/input.txt";
        readInput(inputPath);
        String part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public String getPart1() {
        List<Integer> basePattern = Arrays.asList(0, 1, 0, -1);

        List<Integer> input = in.chars().mapToObj(c -> Integer.parseInt((char) c + "")).collect(Collectors.toList());
        for (int i = 0; i < 100; i++) {
            input = FFT(input, basePattern);
        }
        return input.subList(0,8).stream().reduce("", (a,b) -> a+""+b, (s1,s2)-> s1+s2);
    }

    public List<Integer> FFT(List<Integer> input, List<Integer> basePattern) {
        List<Integer> output = new ArrayList<>();
        for (int i = 0; i <input.size(); i++) {
            output.add(calculate(input, patternForIndex(basePattern, i)));
        }
        return output;
    }


    public List<Integer> patternForIndex(List<Integer> basePattern, int index) {
        List<Integer> result = IntStream.range(0,basePattern.size())
                .mapToObj(i-> IntStream.rangeClosed(0,index)
                        .mapToObj(t-> basePattern.get(i))
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return repeatAndShiftPattern(result);
    }

    private List<Integer> repeatAndShiftPattern(List<Integer> basePattern) {
        return IntStream.rangeClosed(0, in.length()+1)
                .mapToObj(i -> basePattern.get(i % basePattern.size()))
                .collect(Collectors.toList()).subList(1,in.length()+1);
    }

    public int calculate(List<Integer> input, List<Integer> pattern){
        return Math.abs(IntStream.range(0,input.size()).map(i-> input.get(i)*pattern.get(i)).sum())%10;
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {
        in = Input.getSingleLine(inputPath);
    }
}
