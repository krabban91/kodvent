package krabban91.kodvent.kodvent.y2018.d12;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class PlantGenerations {
    Map<Long, Boolean> state = new HashMap<>();
    List<PlantPattern> patterns;
    Map<Long, Long> plantSumPerGeneration = new HashMap<>();
    long currentGeneration = 0;
    Long convergingStepValue;
    Long convergingGeneration;

    public PlantGenerations(List<String> input) {
        String state = input.remove(0);
        AtomicInteger inc = new AtomicInteger(0);
        state.chars().forEach(c -> {
            if (c == (int) '#') {
                this.state.put((long) inc.getAndIncrement(), true);
            } else if (c == (int) '.') {
                this.state.put((long) inc.getAndIncrement(), false);
            }
        });
        padStateWithEmpties();
        patterns = input.stream().filter(s -> s.contains("=>")).map(PlantPattern::new).collect(Collectors.toList());
    }

    public void ageOneGeneration() {
        Map<Long, Boolean> newState = state.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> producesPlant(e.getKey())));
        if (currentGeneration > 1 && areSamePotsWithOffset(state, newState)) {
            convergingGeneration = currentGeneration;
            convergingStepValue = getSumOfState(newState) - getSumOfState(state);
        } else {
            state = newState;
            padStateWithEmpties();
            currentGeneration++;
        }
        countPlantsThisGeneration();
    }

    public long getGenerationScore(long generation) {
        if (!this.hasConverged()) {
            if (!this.plantSumPerGeneration.containsKey(generation)) {
                this.ageUntilGeneration(generation);
            }
        }
        if (this.hasConverged() && convergingGeneration < generation) {
            return (generation - convergingGeneration) * convergingStepValue + this.plantSumPerGeneration.get(convergingGeneration);
        }
        return this.plantSumPerGeneration.get(generation);
    }

    private void printGeneration() {
        StringBuilder b = new StringBuilder();
        b.append(currentGeneration);
        b.append(": ");
        state.entrySet()
                .stream()
                .sorted(Comparator.comparingLong(Map.Entry::getKey))
                .forEachOrdered(e -> b.append(e.getValue() ? '#' : '.'));
        System.out.println(b.toString());
    }

    private void countPlantsThisGeneration() {
        plantSumPerGeneration.put(
                currentGeneration,
                getSumOfState(state));
    }

    private Long getSumOfState(Map<Long, Boolean> state) {
        return state.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .reduce(0L, (l, r) -> l + r.getKey(), Long::sum);
    }

    private boolean areSamePotsWithOffset(Map<Long, Boolean> state, Map<Long, Boolean> newState) {
        long minFirst = state.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .min(Comparator.comparingLong(e -> e.getKey()))
                .get()
                .getKey();
        long minSecond = newState.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .min(Comparator.comparingLong(e -> e.getKey()))
                .get()
                .getKey();
        long diff = minSecond - minFirst;
        return state.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .allMatch(oldPot -> newState.entrySet()
                        .stream()
                        .filter(Map.Entry::getValue)
                        .anyMatch(newPot -> Long.compare(
                                newPot.getKey() - diff,
                                oldPot.getKey()) == 0));
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
        Long max = state.entrySet().stream().filter(Map.Entry::getValue).max(Comparator.comparingLong(Map.Entry::getKey)).get().getKey();
        Long min = state.entrySet().stream().filter(Map.Entry::getValue).min(Comparator.comparingLong(Map.Entry::getKey)).get().getKey();
        LongStream.rangeClosed(max + 1, max + 2).forEach(e -> state.put(e, false));
        LongStream.rangeClosed(min - 2, min - 1).forEach(e -> state.put(e, false));
    }


    private void ageUntilGeneration(long generation) {
        countPlantsThisGeneration();
        printGeneration();
        while (currentGeneration != generation && !this.hasConverged()) {
            ageOneGeneration();
            countPlantsThisGeneration();
            printGeneration();
        }
    }

    private boolean hasConverged() {
        return convergingGeneration != null;
    }
}
