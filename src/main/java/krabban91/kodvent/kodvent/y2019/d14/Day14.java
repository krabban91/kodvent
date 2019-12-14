package krabban91.kodvent.kodvent.y2019.d14;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Day14 {
    List<Reaction> in;

    public Day14() {
        System.out.println("::: Starting Day 14 :::");
        String inputPath = "y2019/d14/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        final Map<String, Reaction> collect = in.stream().collect(Collectors.toMap(e -> e.output, e -> e));
        final Reaction fuel = collect.get("FUEL");
        Map<String, Integer> currentMaterials = new HashMap<>();
        currentMaterials.put(fuel.output, fuel.quantity);
        // 1 fuel -> 44 XJWVT, 5 KHKGT
        // 44 XJWVT -> 44*(7 DCFZ, 7 PSHF)
        final ConcurrentReferenceHashMap<String, Integer> current = new ConcurrentReferenceHashMap<>();
        current.put(fuel.output, 1);
        final long ore = reverseEngineerMolecule(currentMaterials, Set.of("ORE"), in);
        Map<String, Set<String>> reactionMap = in.stream().collect(Collectors.toMap(e -> e.output, e -> e.input.keySet()));
        
        return getOreCount(fuel, collect, current);

        //65007L is wrong. 198072697914L is wrong
    }

    private long reverseEngineerMolecule(Map<String, Integer> start, Set<String> target, List<Reaction> transitions) {
        long bestScenario = Long.MAX_VALUE;
        for (int i = 0; i < 1000; i++) {
            Map<String, Integer> current = new HashMap<>(start);
            while (!current.keySet().equals(target)) {
                Collections.shuffle(transitions);
                Map<String, Integer> tmp = new HashMap<>(current);
                for (Reaction rea : transitions) {
                    if (current.containsKey(rea.output)) {
                        final Integer integer = current.get(rea.output);
                        final int left = integer - rea.quantity;
                        if (left < 0) {
                            if (rea.input.size() == 1 && rea.input.containsKey("ORE")) {
                                current.remove(rea.output);
                                current.computeIfPresent("ORE", (k, v) -> v + rea.input.get("ORE"));
                                current.putIfAbsent("ORE", rea.input.get("ORE"));
                            }
                            continue;
                        } else if (left == 0) {
                            current.remove(rea.output);
                        } else {
                            current.computeIfPresent(rea.output, (k, v) -> left);
                        }
                        Map<String, Integer> finalCurrent = current;
                        rea.input.forEach((k, v) -> {
                            finalCurrent.putIfAbsent(k, v);
                            finalCurrent.computeIfPresent(k, (kk, vv) -> vv + v);
                        });
                    }
                }

                if (current.equals(tmp)) {
                    current = start;
                    Collections.shuffle(transitions);
                }
            }
            bestScenario = Math.min(bestScenario, current.get("ORE"));
        }
        return bestScenario;
    }

    private long getOreCount(Reaction reaction, Map<String, Reaction> collect, ConcurrentReferenceHashMap<String, Integer> current) {
        current.putIfAbsent(reaction.output, reaction.quantity);
        current.computeIfPresent(reaction.output, (k, v) -> v + reaction.quantity);

        return reaction.input.entrySet().stream().mapToLong(e -> {
            if (e.getKey().equals(("ORE"))) {
                return e.getValue();
            }
            if (current.containsKey(e.getKey())) {
                final Integer integer = current.get(e.getKey());
                if (integer > e.getValue()) {

                }
            }
            return e.getKey().equals("ORE") ? e.getValue() :
                    e.getValue() * getOreCount(collect.get(e.getKey()), collect, current);
        }).sum();

    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {

        in = Input.getLines(inputPath).stream()
                .map(Reaction::new)
                .collect(Collectors.toList());
    }
}
