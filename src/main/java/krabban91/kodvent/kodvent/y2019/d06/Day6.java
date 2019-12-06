package krabban91.kodvent.kodvent.y2019.d06;

import krabban91.kodvent.kodvent.utilities.Input;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Day6 {
    List<OrbitRelation> in;

    public Day6() {
        System.out.println("::: Starting Day 6 :::");
        String inputPath = "y2019/d06/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        Map<String, Set<String>> isOrbitedBy = getOrbits();
        Map<String, Integer> countOfOrbits = new ConcurrentReferenceHashMap<>();
        isOrbitedBy.keySet().forEach(e -> countOfOrbits.computeIfAbsent(e, k -> countOrbits(k, isOrbitedBy, countOfOrbits)));
        return countOfOrbits.values().stream().mapToInt(i -> i).sum();
    }

    @NotNull
    public Map<String, Set<String>> getOrbits() {
        Map<String, Set<String>> isOrbitedBy = new HashMap<>();
        in.forEach(o -> {
            isOrbitedBy.computeIfAbsent(o.main, k -> new HashSet<>());
            isOrbitedBy.computeIfPresent(o.main, (k, v) -> {
                v.add(o.orbiter);
                return v;
            });
        });
        return isOrbitedBy;
    }

    @NotNull
    public int countOrbits(String k, Map<String, Set<String>> isOrbitedBy, Map<String, Integer> counts) {
        Set<String> strings = isOrbitedBy.get(k);
        if (strings == null) {
            return 0;
        }
        return strings.size() + strings.stream().mapToInt(v -> counts.computeIfAbsent(v, l -> countOrbits(l, isOrbitedBy, counts))).sum();
    }

    public long getPart2() {
        Map<String, Set<String>> isOrbitedBy = getOrbits();
        return stepsBetween("YOU", "SAN", 0, isOrbitedBy);
    }

    @NotNull
    public int stepsBetween(String current, String target, int stepsTaken, Map<String, Set<String>> isOrbitedBy) {
        if(current.equals(target)){
            return stepsTaken-2;
        }
        if(indirectOrbit(target, current, isOrbitedBy)){
            //move out
            String newCurrent = isOrbitedBy.get(current).stream().filter(s-> indirectOrbit(target,s,isOrbitedBy)).findFirst().get();
            return stepsBetween(newCurrent, target, stepsTaken+1, isOrbitedBy);
        }
        else{
            //move in
            String newCurrent = in.stream().filter(rel -> rel.orbiter.equals(current)).findFirst().get().main;
            return stepsBetween(newCurrent, target, stepsTaken+1, isOrbitedBy);
        }
    }

    public boolean indirectOrbit(String target, String current, Map<String, Set<String>> isOrbitedBy) {
        if(target.equals(current)){
            return true;
        }
        if (!isOrbitedBy.containsKey(current)) {
            return false;
        }
        return isOrbitedBy.get(current).stream().anyMatch(s -> s.equals(target) || indirectOrbit(target, s, isOrbitedBy));
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath)
                .stream()
                .map(OrbitRelation::new)
                .collect(Collectors.toList());
    }
}
