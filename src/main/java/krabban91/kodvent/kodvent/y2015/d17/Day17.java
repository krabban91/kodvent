package krabban91.kodvent.kodvent.y2015.d17;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day17 {
    List<Container> in;

    public Day17() {
        System.out.println("::: Starting Day 17 :::");
        String inputPath = "y2015/d17/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        return waysOfTheEggNod(in, 150, new ConcurrentReferenceHashMap<>(), new ConcurrentReferenceHashMap<>());
    }

    public int waysOfTheEggNod(List<Container> containerSizes, int capacity,
                               ConcurrentReferenceHashMap<List<Container>, Integer> history,
                               ConcurrentReferenceHashMap<List<Container>, Integer> capacityHistory) {
        List<Container> key = containerSizes.stream().sorted(Comparator.comparingInt(Container::getSize)).collect(Collectors.toList());
        if (capacity < 0) {
            return history.computeIfAbsent(key, k -> 0);
        }
        if (capacity == 0) {
            capacityHistory.putIfAbsent(key, capacity);
            return history.computeIfAbsent(key, k -> 1);
        }
        return containerSizes.stream().mapToInt(c -> {
            ArrayList<Container> objects = new ArrayList<>(containerSizes);
            objects.remove(c);
            List<Container> list = objects.stream().sorted(Comparator.comparingInt(Container::getSize)).collect(Collectors.toList());
            if (history.containsKey(list)) {
                return 0;
            }
            return history.computeIfAbsent(list, k -> waysOfTheEggNod(k, capacity - c.getSize(), history, capacityHistory));
        }).sum();
    }


    public long getPart2() {
        ConcurrentReferenceHashMap<List<Container>, Integer> history = new ConcurrentReferenceHashMap<>();
        waysOfTheEggNod(in, 150, new ConcurrentReferenceHashMap<>(), history);
        return bestWaysOfTheEggNod(history);
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).stream().map(Integer::parseInt).map(Container::new).collect(Collectors.toList());
    }

    public long bestWaysOfTheEggNod(ConcurrentReferenceHashMap<List<Container>, Integer> history) {
        List<Map.Entry<List<Container>, Integer>> collect = history.entrySet().stream().filter(e -> e.getValue() == 0).collect(Collectors.toList());
        int i = collect.stream().map(Map.Entry::getKey).mapToInt(List::size).max().orElse(-1);
        long count = collect.stream().map(Map.Entry::getKey).mapToInt(List::size).filter(v -> v == i).count();
        return count;
    }
}
