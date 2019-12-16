package krabban91.kodvent.kodvent.y2019.d16;

import krabban91.kodvent.kodvent.utilities.Input;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

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
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        in = "12345678";
        List<Integer> basePattern = Arrays.asList(0, 1, 0, -1);
        final List<Integer> pattern = firstPattern(basePattern);
        final List<Integer> pattern1 = repeatPattern(pattern, basePattern, 1);
        final List<Integer> pattern2 = repeatPattern(pattern1, basePattern, 2);
        final List<Integer> input = in.chars().mapToObj(c -> Integer.parseInt((char) c + "")).collect(Collectors.toList());
        return -1L;
    }

    public List<Integer> FFT(List<Integer> input, List<Integer> basePattern) {
        return null;
    }

    private List<Integer> firstPattern(List<Integer> basePattern) {
        final List<Integer> collect = fullBasePattern(basePattern);
        return collect.subList(1, in.length() + 1);
    }

    @NotNull
    private List<Integer> fullBasePattern(List<Integer> basePattern) {
        return IntStream.rangeClosed(0, in.length())
                .mapToObj(i -> basePattern.get(i % basePattern.size()))
                .collect(Collectors.toList());
    }

    private List<Integer> repeatPattern(List<Integer> pattern, List<Integer> basePattern, int index){
        final List<Integer> pattern1 = fullBasePattern(basePattern);
        return IntStream.range(0,pattern.size()).mapToObj(i-> i<index? +0:Math.abs(pattern.get(i))+pattern1.get(i+1)).collect(Collectors.toList());
    }

    public List<Integer> patternForIndex(List<Integer> basePattern, int index){

        final List<Integer> pattern = fullBasePattern(basePattern);
        List<Integer> result = firstPattern(basePattern);
        for(int iter = 0; iter<index; iter++){
            List<Integer> finalResult = result;
            result =  IntStream.range(0,result.size())
                    .mapToObj(i-> i<index? +0: finalResult.get(i)+pattern.get(i+1))
                    .collect(Collectors.toList());

        }
        return result;
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {
        in = Input.getSingleLine(inputPath);
    }
}
