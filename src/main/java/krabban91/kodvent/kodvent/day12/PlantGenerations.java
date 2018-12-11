package krabban91.kodvent.kodvent.day12;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class PlantGenerations {
    Map<Long, Boolean> state = new HashMap<>();
    List<PlantPattern> patterns;
    Map<Long, Long> plantSumPerGeneration = new HashMap<>();
    long currentGeneration = 0;

    public PlantGenerations(Stream<String> input) {
        List<String> collect = input.collect(Collectors.toList());
        String state = collect.remove(0);
        AtomicInteger inc = new AtomicInteger(0);
        state.chars().forEach(c -> {
            if (c == (int) '#') {
                this.state.put((long) inc.getAndIncrement(), true);
            } else if (c == (int) '.') {
                this.state.put((long) inc.getAndIncrement(), false);
            }
        });
        padStateWithEmpties();
        patterns = collect.stream().filter(s -> s.contains("=>")).map(PlantPattern::new).collect(Collectors.toList());
    }

    public void countPlantsThisGeneration() {
        plantSumPerGeneration.put(
                currentGeneration,
                state.entrySet()
                        .stream()
                        .filter(e -> e.getValue())
                        .reduce(0L, (l, r) -> l + r.getKey(), Long::sum));
    }

    public void ageOneGeneration() {
        state = state.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> producesPlant(e.getKey())));
        padStateWithEmpties();
        currentGeneration++;
    }

    private boolean producesPlant(long key) {
        List<Boolean> pots = new ArrayList<>();
        pots.add(state.getOrDefault(key - 2, false));
        pots.add(state.getOrDefault(key - 1, false));
        pots.add(state.getOrDefault(key, false));
        pots.add(state.getOrDefault(key + 1, false));
        pots.add(state.getOrDefault(key + 2, false));
        Optional<PlantPattern> any = patterns.stream().filter(p -> p.matches(pots)).findAny();
        return any.isPresent() ? any.get().getProducesPlant() : false;
    }

    private void padStateWithEmpties() {
        Long max = state.entrySet().stream().filter(e -> e.getValue()).max(Comparator.comparingLong(e -> e.getKey())).get().getKey();
        Long min = state.entrySet().stream().filter(e -> e.getValue()).min(Comparator.comparingLong(e -> e.getKey())).get().getKey();
        LongStream.rangeClosed(max + 1, max + 4).forEach(e -> state.put(e, false));
        LongStream.rangeClosed(min - 4, min - 1).forEach(e -> state.put(e, false));
    }

    public void printGeneration() {
        StringBuilder b = new StringBuilder();
        b.append(currentGeneration);
        b.append(": ");
        state.entrySet().stream().sorted(Comparator.comparingLong(e -> e.getKey())).forEachOrdered(e -> {
            if (e.getValue()) {
                b.append('#');
            } else {
                b.append('.');
            }
        });
        System.out.println(b.toString());

    }
    public void printGenerationScore(){
        StringBuilder b = new StringBuilder();
        b.append(currentGeneration);
        b.append(": score= ");
        b.append(getGenerationScore(currentGeneration));
        System.out.println(b.toString());
    }

    public long getGenerationScore(long generation) {
        return this.plantSumPerGeneration.get(generation);
    }

    public void ageUntilGeneration(long generation) {
        countPlantsThisGeneration();
        printGeneration();
        while (currentGeneration != generation) {
            ageOneGeneration();
            countPlantsThisGeneration();
            printGenerationScore();
            if (currentGeneration % 100000 == 0) {
                printGeneration();

            }
        }
    }


}
