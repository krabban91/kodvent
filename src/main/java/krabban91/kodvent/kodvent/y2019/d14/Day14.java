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
        //long part1 = getPart1();
        //System.out.println(": answer to part 1 :");
        //System.out.println(part1);
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
        return reverseEngineerMolecule(currentMaterials, Set.of("ORE"), in);

        //65007L is wrong. 198072697914L is wrong, 85254 is too low
    }

    private long reverseEngineerMolecule(Map<String, Integer> start, Set<String> target, List<Reaction> transitions) {
        long bestScenario = Long.MAX_VALUE;
        final List<Reaction> oreT = transitions.stream().filter(r -> r.input.containsKey("ORE")).collect(Collectors.toList());
        final List<Reaction> other = transitions.stream().filter(r -> !r.input.containsKey("ORE")).collect(Collectors.toList());
        for (int i = 0; i < 100; i++) {  // try more times in case there are better ways.
            Map<String, Integer> current = new HashMap<>(start);
            while (!current.keySet().equals(target)) {
                Collections.shuffle(transitions);
                Map<String, Integer> tmp = new HashMap<>(current);
                if (current.keySet().stream().allMatch(s -> oreT.stream().anyMatch(r -> r.output.equals(s)))) {
                    while (!current.keySet().equals(target)) {
                        for (Reaction rea : oreT) {
                            if (current.containsKey(rea.output)) {
                                final Integer outQuantity = current.get(rea.output);
                                final int left = outQuantity - rea.quantity;
                                if (rea.input.containsKey("ORE")) {
                                    if (left <= 0) {
                                        current.remove(rea.output);
                                    } else {
                                        current.computeIfPresent(rea.output, (k, v) -> v - rea.quantity);
                                    }
                                    current.computeIfPresent("ORE", (k, v) -> v + rea.input.get("ORE"));
                                    current.putIfAbsent("ORE", rea.input.get("ORE"));
                                }
                            }
                        }
                    }
                }
                for (Reaction rea : other) {
                    if (current.containsKey(rea.output)) {
                        final Integer outQuantity = current.get(rea.output);
                        final int left = outQuantity - rea.quantity;
                        {
                            int factor = 1;
                            if (left <= 0) {
                                current.remove(rea.output);
                                int tmpVal = 0;
                                int tmpFactor = 0;
                                while (tmpVal<rea.quantity){
                                    tmpVal +=outQuantity;
                                    tmpFactor++;
                                }
                                factor = tmpFactor;

                            } else {
                                current.computeIfPresent(rea.output, (k, v) -> left);
                            }
                            Map<String, Integer> finalCurrent = current;
                            int finalFactor = factor;
                            rea.input.forEach((k, v) -> {
                                finalCurrent.computeIfPresent(k, (kk, vv) -> vv + finalFactor * v);
                                finalCurrent.putIfAbsent(k, finalFactor * v);
                            });
                        }
                    }
                }
                if (current.equals(tmp)) {
                    current = new HashMap<>(start);
                    Collections.shuffle(transitions);
                }
            }
            bestScenario = Math.min(bestScenario, current.get("ORE"));
        }
        return bestScenario;
        /*
        for (Reaction rea : transitions) {
                    if (current.containsKey(rea.output)) {
                        final Integer outQuantity = current.get(rea.output);
                        final int left = outQuantity - rea.quantity;
                        if (rea.input.containsKey("ORE")) {
                            if (left <= 0) {
                                current.remove(rea.output);
                            } else {
                                current.computeIfPresent(rea.output, (k, v) -> v - rea.quantity);
                            }
                            current.computeIfPresent("ORE", (k, v) -> v + rea.input.get("ORE"));
                            current.putIfAbsent("ORE", rea.input.get("ORE"));
                        } else {
                            int factor = 1;
                            if (left < 0) {
                                current.remove(rea.output);
                                factor = rea.quantity / outQuantity + 1;
                            } else if (left == 0) {
                                current.remove(rea.output);
                            } else {
                                current.computeIfPresent(rea.output, (k, v) -> left);
                            }
                            Map<String, Integer> finalCurrent = current;
                            int finalFactor = factor;
                            rea.input.forEach((k, v) -> {
                                finalCurrent.computeIfPresent(k, (kk, vv) -> vv + finalFactor *v);
                                finalCurrent.putIfAbsent(k, finalFactor *v);
                            });
                            int a = 0;
                        }
                    }
                }

                for (Reaction rea : oreT) {
                    if (current.containsKey(rea.output)) {
                        final Integer integer = current.get(rea.output);
                        final int left = integer - rea.quantity;
                        if (rea.input.containsKey("ORE")) {

                            if (left <= 0) {
                                current.remove(rea.output);
                            } else {
                                current.computeIfPresent(rea.output, (k, v) -> v - rea.quantity);
                            }
                            current.computeIfPresent("ORE", (k, v) -> v + rea.input.get("ORE"));
                            current.putIfAbsent("ORE", rea.input.get("ORE"));
                        }
                    }
                }
                }

                for (Reaction rea : other) {
                    if (current.containsKey(rea.output)) {
                        final Integer integer = current.get(rea.output);
                        final int left = integer - rea.quantity;
                        if (rea.input.containsKey("ORE")) {

                            if (left <= 0) {
                                current.remove(rea.output);
                            } else {
                                current.computeIfPresent(rea.output, (k, v) -> v - rea.quantity);
                            }
                            current.computeIfPresent("ORE", (k, v) -> v + rea.input.get("ORE"));
                            current.putIfAbsent("ORE", rea.input.get("ORE"));
                        } else {
                            int factor = 1;
                            if (left < 0) {
                                current.remove(rea.output);
                                factor = rea.quantity / integer + 1;
                            } else if (left == 0) {
                                current.remove(rea.output);
                            } else {
                                current.computeIfPresent(rea.output, (k, v) -> left);
                            }
                            Map<String, Integer> finalCurrent = current;
                            int finalFactor = factor;
                            rea.input.forEach((k, v) -> {
                                finalCurrent.computeIfPresent(k, (kk, vv) -> vv + finalFactor *v);
                                finalCurrent.putIfAbsent(k, finalFactor *v);
                            });
                            int a = 0;
                        }
                    }
                }*/
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
