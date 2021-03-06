package krabban91.kodvent.kodvent.y2019.d06;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    private static Map<String, Set<String>> getOrbits(List<OrbitRelation> relations) {
        Map<String, Set<String>> isOrbitedBy = new HashMap<>();
        relations.forEach(o -> {
            isOrbitedBy.computeIfAbsent(o.main, k -> new HashSet<>());
            isOrbitedBy.computeIfPresent(o.main, (k, v) -> {
                v.add(o.orbiter);
                return v;
            });
        });
        return isOrbitedBy;
    }

    private static boolean indirectOrbit(String target, String current, Map<String, Set<String>> isOrbitedBy) {
        if (target.equals(current)) {
            return true;
        }
        if (!isOrbitedBy.containsKey(current)) {
            return false;
        }
        return isOrbitedBy.get(current).stream().anyMatch(s -> s.equals(target) || indirectOrbit(target, s, isOrbitedBy));
    }

    public long getPart1() {
        Map<String, Set<String>> isOrbitedBy = getOrbits(in);
        Map<String, Integer> countOfOrbits = new ConcurrentReferenceHashMap<>();
        isOrbitedBy.keySet().forEach(e -> countOfOrbits.computeIfAbsent(e, k -> celestialBodysOf(k, isOrbitedBy, countOfOrbits)));
        return countOfOrbits.values().stream().mapToInt(i -> i).sum();
    }

    public long getPart2() {
        Map<String, Set<String>> orbitedBy = getOrbits(in);
        return orbitalDistance("YOU", "SAN", 0, orbitedBy) - 2;
    }

    private int celestialBodysOf(String current, Map<String, Set<String>> isOrbitedBy, Map<String, Integer> counts) {
        Set<String> bodys = isOrbitedBy.get(current);
        if (bodys == null) {
            return 0;
        }
        return bodys.size() + bodys.stream()
                .mapToInt(body -> counts.computeIfAbsent(body, key -> celestialBodysOf(key, isOrbitedBy, counts)))
                .sum();
    }

    private int orbitalDistance(String current, String target, int stepsTaken, Map<String, Set<String>> isOrbitedBy) {
        if (current.equals(target)) {
            return stepsTaken;
        }
        if (indirectOrbit(target, current, isOrbitedBy)) {
            //move out
            String newCurrent = isOrbitedBy.get(current).stream().filter(s -> indirectOrbit(target, s, isOrbitedBy)).findFirst().get();
            return orbitalDistance(newCurrent, target, stepsTaken + 1, isOrbitedBy);
        } else {
            //move in
            String newCurrent = in.stream().filter(rel -> rel.orbiter.equals(current)).findFirst().get().main;
            return orbitalDistance(newCurrent, target, stepsTaken + 1, isOrbitedBy);
        }
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath)
                .stream()
                .map(OrbitRelation::new)
                .collect(Collectors.toList());
    }
}
