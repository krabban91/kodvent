package krabban91.kodvent.kodvent.y2015.d13;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day13 {
    List<HappinessDelta> in;

    public Day13() {
        System.out.println("::: Starting Day 13 :::");
        String inputPath = "y2015/d13/input.txt";
        in = readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        return getBestSeating(in);
    }

    public long getPart2() {
        ArrayList<HappinessDelta> happinessDeltas = inputWithMe();

        return getBestSeating(happinessDeltas);
    }

    private ArrayList<HappinessDelta> inputWithMe() {
        ArrayList<HappinessDelta> happinessDeltas = new ArrayList<>(in);
        List<String> persons = in.stream().map(HappinessDelta::getPerson).collect(Collectors.toList());
        persons.forEach(p -> happinessDeltas.add(new HappinessDelta(0, "me", p)));
        persons.forEach(p -> happinessDeltas.add(new HappinessDelta(0, p, "me")));
        return happinessDeltas;
    }

    private long getBestSeating(List<HappinessDelta> input) {
        List<String> persons = input.stream().map(HappinessDelta::getPerson).distinct().collect(Collectors.toList());
        return allPossibleSeatings(input, persons, new LinkedList<>()).stream()
                .mapToLong(l -> getTotalHappiness(l, input))
                .max()
                .orElse(-1);
    }

    private List<List<String>> allPossibleSeatings(List<HappinessDelta> input, List<String> persons, List<String> seating) {
        if (persons.isEmpty()) {
            if (validateSeating(seating, input)) {
                return Collections.singletonList(seating);
            }
            return Collections.emptyList();
        }
        return persons.stream()
                .flatMap(p -> {
                    List<String> nextSeating = new ArrayList<>(seating);
                    nextSeating.add(p);
                    return allPossibleSeatings(input, persons.stream().filter(p1 -> !p1.equals(p)).collect(Collectors.toList()), nextSeating).stream();
                })
                .filter(l -> !l.isEmpty())
                .collect(Collectors.toList());
    }

    public boolean validateSeating(List<String> seating, List<HappinessDelta> input) {
        return IntStream
                .range(0, seating.size())
                .allMatch(i -> input.stream().anyMatch(r -> r.getPerson().equals(seating.get(i)) &&
                        r.getNextTo().equals(seating.get((i + 1) % seating.size()))) &&
                        input.stream().anyMatch(r -> r.getPerson().equals(seating.get(i)) &&
                                r.getNextTo().equals(seating.get((i - 1 + seating.size()) % seating.size()))));
    }

    private long getTotalHappiness(List<String> seating, List<HappinessDelta> input) {
        return IntStream
                .range(0, seating.size())
                .mapToLong(i -> input.stream().filter(delta -> delta.getPerson().equals(seating.get(i)) &&
                        delta.getNextTo().equals(seating.get((i + 1) % seating.size()))).findFirst().get().getChange())
                .sum();
    }

    public List<HappinessDelta> readInput(String inputPath) {
        return Input
                .getLines(inputPath)
                .stream()
                .map(HappinessDelta::new)
                .collect(Collectors.toList());
    }
}
