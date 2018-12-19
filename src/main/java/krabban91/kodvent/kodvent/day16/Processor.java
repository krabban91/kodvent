package krabban91.kodvent.kodvent.day16;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Processor {

    List<OpCodeSample> collect;

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
        List<String> secondHalfOfSample = inputRows.subList(indexOfSplit.get(), inputRows.size());
        collect = IntStream.range(0, firstHalfOfSample.size() / 4).mapToObj(i -> {
            int index = i * 4;
            return new OpCodeSample(firstHalfOfSample.get(index), firstHalfOfSample.get(index + 1), firstHalfOfSample.get(index + 2));
        }).collect(Collectors.toList());
    }

    public int numberOfSamplesMatchingMoreThanOrEqualTo(int count){
        OpCodesALU alu = new OpCodesALU();
        return (int)collect.stream().map(sample -> OpCodeSample.matchesForNumberOfOpCodes(alu, sample)).filter(c -> c >= count).count();
    }
}
