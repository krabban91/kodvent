package krabban91.kodvent.kodvent.day16;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Processor {

    List<OpCodeSample> samples;
    List<Operation> operations;
    OpCodesALU alu = new OpCodesALU();
    Map<Integer, Integer> codeTransitions;

    public Processor(List<String> inputRows) {
        AtomicBoolean previousWasEmpty = new AtomicBoolean(false);
        AtomicInteger indexOfSplit = new AtomicInteger(0);
        IntStream.range(0, inputRows.size()).forEach(i -> {
            boolean currentIsEmpty = inputRows.get(i).replace(" ", "").isEmpty();
            if (previousWasEmpty.get()) {
                if (currentIsEmpty) {
                    indexOfSplit.set(i);
                    previousWasEmpty.set(false);
                } else {
                    previousWasEmpty.set(currentIsEmpty);
                }
            } else {
                if (currentIsEmpty) {
                    previousWasEmpty.set(currentIsEmpty);
                }
            }
        });
        List<String> firstHalfOfSample = inputRows.subList(0, indexOfSplit.get());
        List<String> secondHalfOfSample = inputRows.subList(indexOfSplit.get(), inputRows.size()).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        samples = IntStream.range(0, firstHalfOfSample.size() / 4).mapToObj(i -> {
            int index = i * 4;
            return new OpCodeSample(firstHalfOfSample.get(index), firstHalfOfSample.get(index + 1), firstHalfOfSample.get(index + 2));
        }).collect(Collectors.toList());
        operations = secondHalfOfSample.stream().map(Operation::new).collect(Collectors.toList());
        Map<Integer, List<Integer>> collect = getPotentialTransitions();
        codeTransitions = setUpTransitions(collect);
    }

    private Map<Integer, List<Integer>> getPotentialTransitions() {
        return samples.stream()
                .collect(Collectors.groupingBy(s -> s.operation.operation))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue()
                                .stream()
                                .map(OpCodeSample::getWorksForOperation)
                                .map(m -> m.entrySet())
                                .flatMap(Set::stream)
                                .collect(Collectors.groupingBy(
                                        Map.Entry::getKey,
                                        Collectors.mapping(
                                                Map.Entry::getValue,
                                                Collectors.toList())))
                                .entrySet()
                                .stream()
                                .filter(es -> es.getValue().stream().allMatch(b -> b))
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList())
                ));
    }

    private Map<Integer, Integer> setUpTransitions(Map<Integer, List<Integer>> collect) {
        Map<Integer, Integer> codeTransitions = new HashMap<>();
        while (codeTransitions.keySet().size() < collect.keySet().size()) {
            collect.keySet().stream().forEach(e -> {
                if (collect.get(e).size() == 1) {
                    Integer put = codeTransitions.put(e, collect.get(e).get(0));
                    collect.keySet().forEach(i -> collect.get(i).removeAll(Collections.singleton(put)));
                }
            });
        }
        return codeTransitions;
    }

    public int numberOfSamplesMatchingMoreThanOrEqualTo(int count) {
        OpCodesALU alu = new OpCodesALU();
        return (int) samples.stream()
                .map(OpCodeSample::getWorksForOperation)
                .map(m -> m.values().stream().filter(e -> e).count())
                .filter(c -> c >= count)
                .count();
    }

    public long valueAtRegisterAfterOperations(int i) {
        return operations.stream().reduce(new int[]{0, 0, 0, 0}, (l, r) -> OpCodesALU.executeOp(alu, l, codeTransitions.get(r.operation), r.a, r.b, r.c),
                (l, r) -> l)[i];
    }
}
