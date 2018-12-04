package krabban91.kodvent.kodvent.day4;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Component
public class GuardLogger {

    private static String inputPath = "day4.txt";
    private Map<Integer, Map<Integer, List<String>>> guardEntries = new HashMap<>();
    private Integer currentGuard = null;
    private String currentDay = null;
    private int momentOfFallingAsleep;

    public GuardLogger() {

        System.out.println("::: Starting Day 4:::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            int part1 = getPart1(stream);
            System.out.println(": answer to part 1 :");
            System.out.println(part1);
            int part2 = getPart2();
            System.out.println(": answer to part 2 :");
            System.out.println(part2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPart1(Stream<String> stream) {
        stream
                .sorted(String::compareTo)
                .map(TimeEntry::new)
                .forEachOrdered(this::logEntry);
        Map.Entry<Integer, Map<Integer, List<String>>> max = getGuardThatSleepsTheMostMinutes();
        int guard = max.getKey();
        Map.Entry<Integer, List<String>> bestMinute = getMinuteEntryWithMostOverlaps(max);
        int minute = bestMinute.getKey();
        return guard * minute;
    }

    public int getPart2() {
        Map.Entry<Integer, Map<Integer, List<String>>> max = getGuardThatSleepsMostAtACertainMinute();
        int guard = max.getKey();
        Map.Entry<Integer, List<String>> bestMinute = getMinuteEntryWithMostOverlaps(max);
        int minute = bestMinute.getKey();
        return guard * minute;
    }

    private Map.Entry<Integer, List<String>> getMinuteEntryWithMostOverlaps(Map.Entry<Integer, Map<Integer, List<String>>> max) {
        return max.getValue().entrySet()
                .stream()
                .max(Comparator.comparingInt(e -> e.getValue().size())).get();
    }

    private Map.Entry<Integer, Map<Integer, List<String>>> getGuardThatSleepsMostAtACertainMinute() {
        return guardEntries
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(this::getcompareValue))
                .get();
    }

    private int getcompareValue(Map.Entry<Integer, Map<Integer, List<String>>> e) {
        return e.getValue()
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(val -> val.getValue().size()))
                .get().getValue().size();
    }

    private Map.Entry<Integer, Map<Integer, List<String>>> getGuardThatSleepsTheMostMinutes() {
        return guardEntries
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(e -> e
                        .getValue()
                        .entrySet()
                        .stream()
                        .map(minute -> minute.getValue().size())
                        .reduce(0, Integer::sum)))
                .get();
    }

    private void logEntry(TimeEntry timeEntry) {
        if (timeEntry.getTypeOfEntry() == 0) {
            currentGuard = timeEntry.getGuardId();
            currentDay = timeEntry.getDay();
        }
        if (timeEntry.getTypeOfEntry() == 1) {
            if (!guardEntries.containsKey(currentGuard)) {
                guardEntries.put(currentGuard, new HashMap<>());
            }
            momentOfFallingAsleep = timeEntry.getMinute();
        }
        if (timeEntry.getTypeOfEntry() == 2) {
            int wakeUpTime = timeEntry.getMinute();
            for (int i = momentOfFallingAsleep; i < wakeUpTime; i++) {
                Map<Integer, List<String>> integerIntegerMap = guardEntries.get(currentGuard);
                if (!integerIntegerMap.containsKey(i)) {
                    integerIntegerMap.put(i, new LinkedList<>());
                }
                guardEntries.get(currentGuard).get(i).add(currentDay);
            }
        }
    }
}
