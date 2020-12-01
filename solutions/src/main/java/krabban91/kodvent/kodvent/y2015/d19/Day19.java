package krabban91.kodvent.kodvent.y2015.d19;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day19 {
    List<String> in;

    public Day19() {
        System.out.println("::: Starting Day 19 :::");
        String inputPath = "y2015/d19/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        List<Transition> transitions = getTransitions();
        String Molecule = in.get(in.size() - 1);
        return (long) allPotentialMolecules(Molecule, transitions)
                .size();
    }

    public List<Transition> getTransitions() {
        return in.stream()
                .filter(s -> s.contains("=>"))
                .map(Transition::new)
                .collect(Collectors.toList());
    }

    public List<String> allPotentialMolecules(String molecule, List<Transition> transitions) {
        List<String> collect = transitions.stream()
                .map(t -> t.potentialMolecules(molecule))
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
        return collect;
    }

    public long getPart2() {
        List<Transition> transitions = getTransitions();
        String targetMolecule = in.get(in.size() - 1);
        String startingMolecule = "e";
        return reverseEngineerMolecule(targetMolecule, startingMolecule, transitions);
    }

    private long reverseEngineerMolecule(String start, String target, List<Transition> transitions) {
        long stepsTaken = 0;
        String current = start;
        while (!current.equals(target)) {
            String tmp = current;
            for (Transition t : transitions) {
                if (current.contains(t.getTo())) {
                    current = current.replaceFirst(t.getTo(), t.getFrom());
                    stepsTaken++;
                }
            }
            if (current.equals(tmp)) {
                current = start;
                Collections.shuffle(transitions);
                stepsTaken = 0;
            }
        }
        return stepsTaken;
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath);
    }
}
