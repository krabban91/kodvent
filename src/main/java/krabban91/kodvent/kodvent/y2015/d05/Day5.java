package krabban91.kodvent.kodvent.y2015.d05;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Strings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Component
public class Day5 {
    private List<String> in;
    private Pattern vowels = Pattern.compile(Strings.VOWEL_REGEX);

    public long getPart1() {
        return in.stream().filter(this::isNice).count();
    }

    public long getPart2() {
        return -1;
    }

    private boolean isNice(String s) {
        return containsThreeVowels(s) &&
                hasOneTwinLetter(s) &&
                !containsDisallowedString(s);
    }

    private boolean containsDisallowedString(String s) {
        return s.contains("ab") || s.contains("cd") || s.contains("pq") || s.contains("xy");
    }

    private boolean hasOneTwinLetter(String s) {
        return IntStream.range(0, Strings.ALPHABBET.length())
                .anyMatch(i -> s.contains(Strings.repeated(Strings.ALPHABBET.charAt(i) + "", 2)));
    }

    private boolean containsThreeVowels(String s) {
        return s.chars().filter(this::isVowel).count() >= 3;
    }

    private boolean isVowel(int c) {
        return c == (int) 'a' || c == (int) 'e' || c == (int) 'i' || c == (int) 'o' || c == (int) 'u';
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath);
    }

    public Day5() {
        System.out.println("::: Starting Day 5 :::");
        String inputPath = "y2015/d05/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
