package krabban91.kodvent.kodvent.y2018.d04;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class GuardLogger {

    private Map<Integer, Map<Integer, AtomicInteger>> guardEntries = new HashMap<>();
    private Integer currentGuard = null;
    private int startedSleepingAt;

    public int getPart1() {
        return getResultBasedOnStrategy(this::guardSleepsTheMostMinutes);
    }

    public int getPart2() {
        return getResultBasedOnStrategy(this::guardSleepsMostAtACertainMinute);
    }

    public void logGuardEntries(String path) {
        Input.getLines(path)
                .stream()
                .sorted(String::compareTo)
                .map(TimeEntry::new)
                .forEach(this::logEntry);
    }

    private void logEntry(TimeEntry timeEntry) {
        if (timeEntry.getTypeOfEntry() == 0) {
            currentGuard = timeEntry.getGuardId();
        }
        if (timeEntry.getTypeOfEntry() == 1) {
            if (!guardEntries.containsKey(currentGuard)) {
                guardEntries.put(currentGuard, new HashMap<>());
            }
            startedSleepingAt = timeEntry.getMinute();
        }
        if (timeEntry.getTypeOfEntry() == 2) {
            IntStream
                    .range(startedSleepingAt, timeEntry.getMinute())
                    .forEach(this::logMinute);
        }
    }

    private void logMinute(int minute) {
        Map<Integer, AtomicInteger> integerIntegerMap = guardEntries.get(currentGuard);
        if (!integerIntegerMap.containsKey(minute)) {
            integerIntegerMap.put(minute, new AtomicInteger());
        }
        guardEntries.get(currentGuard).get(minute).addAndGet(1);
    }

    private int getResultBasedOnStrategy(Supplier<Map.Entry<Integer, Map<Integer, AtomicInteger>>> predicate) {
        Map.Entry<Integer, Map<Integer, AtomicInteger>> max = predicate.get();
        int guard = max.getKey();
        Map.Entry<Integer, AtomicInteger> bestMinute = getMinuteEntryWithMostOverlaps(max);
        int minute = bestMinute.getKey();
        return guard * minute;
    }

    private Map.Entry<Integer, AtomicInteger> getMinuteEntryWithMostOverlaps(Map.Entry<Integer, Map<Integer, AtomicInteger>> max) {
        return max.getValue().entrySet()
                .stream()
                .max(Comparator.comparingInt(e -> e.getValue().get())).get();
    }

    private Map.Entry<Integer, Map<Integer, AtomicInteger>> guardSleepsMostAtACertainMinute() {
        return guardEntries
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(this::getBestOverlap))
                .get();
    }

    private Map.Entry<Integer, Map<Integer, AtomicInteger>> guardSleepsTheMostMinutes() {
        return guardEntries
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(this::getMinutesSlept))
                .get();
    }

    private int getMinutesSlept(Map.Entry<Integer, Map<Integer, AtomicInteger>> guard) {
        return guard.getValue()
                .entrySet()
                .stream()
                .map(minute -> minute.getValue().get())
                .reduce(0, Integer::sum);
    }

    private int getBestOverlap(Map.Entry<Integer, Map<Integer, AtomicInteger>> guard) {
        return guard.getValue()
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(minute -> minute.getValue().get()))
                .get().getValue().get();
    }

    public GuardLogger() {
        System.out.println("::: Starting Day 4:::");
        String inputPath = "y2018/d04/input.txt";
        logGuardEntries(inputPath);
        int part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
