package krabban91.kodvent.kodvent.y2018.d05;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Strings;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class AlchemicalReactor {

    private String inputPolymer;
    private Map<String, Integer> score = new HashMap<>();

    public int getPart1() {
        return this.applyReactionFully(inputPolymer).length();
    }

    public int getPart2() {
        IntStream.range(0, Strings.ALPHABBET.length()).forEach(this::retrieveResultForUnit);
        return score.entrySet()
                .stream()
                .min(Comparator.comparingInt(e -> e.getValue()))
                .get().getValue();
    }

    public void readInput(String path) {
        this.inputPolymer = Input.getSingleLine(path);
    }

    private void retrieveResultForUnit(int i) {
        String character = Strings.ALPHABBET.substring(i, i + 1);
        score.put(character, applyReactionFully(inputPolymer
                .replaceAll(String.format("[(%s)(%s)]", character, character.toUpperCase()), ""))
                .length());
    }

    private String applyReactionFully(String polymer) {
        int length = Integer.MAX_VALUE;
        String result = polymer;
        while (length != polymer.length()) {
            length = polymer.length();
            result = this.performReaction(polymer);
        }
        return result;
    }

    private String performReaction(String polymer) {
        return polymer.chars()
                .mapToObj(i -> (char) i)
                .reduce("", this::react, String::concat);
    }

    private String react(String polymer, Character unit) {
        if (polymer.length() != 0) {
            int lastIndex = polymer.length() - 1;
            if (isMatchingUnit(polymer.charAt(lastIndex), unit)) {
                return polymer.substring(0, lastIndex);
            }
        }
        return polymer + unit;
    }

    private boolean isMatchingUnit(Character l, Character r) {
        return !l.equals(r) && (l.equals(Character.toLowerCase(r)) || l.equals(Character.toUpperCase(r)));
    }

    public AlchemicalReactor() {
        System.out.println("::: Starting Day 5:::");
        String inputPath = "y2018/d05/input.txt";
        readInput(inputPath);
        int part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
