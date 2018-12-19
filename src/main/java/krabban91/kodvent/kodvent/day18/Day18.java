package krabban91.kodvent.kodvent.day18;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day18 {
    private static String inputPath = "day18.txt";
    List<List<SettlerTile>> in;
    boolean hasBegunToCycle;
    Integer firstCycleStart;

    Map<Integer, List<List<SettlerTile>>> oldStates = new HashMap<>();
    Map<Integer, Long> scoreOfMiniute = new HashMap<>();
    Map<Integer, Long> cycleScoreDeltas = new HashMap<>();
    int minute = 0;

    public long getPart1() {
        tickMinutes(10);
        return this.scoreOfMiniute.get(10);
    }

    void tickMinutes(int minute) {
        while (this.minute < minute) {
            visualizeLand();
            tickOneMinute(minute);
        }
    }

    void visualizeLand() {
        System.out.println("Second " + minute);
        IntStream.range(0, in.size()).forEach(y -> {
            StringBuilder builder = new StringBuilder();
            in.get(y).forEach(t -> builder.append(t));
            System.out.println(builder.toString());
        });
    }

    void tickOneMinute(int targetMinute) {
        if (!hasBegunToCycle) {

            IntStream.range(0, in.size()).forEach(y -> IntStream.range(0, in.get(y).size()).forEach(x -> {
                SettlerTile current = in.get(y).get(x);
                if (current.oldState == SettlerTile.Type.OPEN) {
                    int adjacentTrees = 0;
                    if (x > 0 && y > 0) {
                        adjacentTrees += in.get(y - 1).get(x - 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (y > 0) {
                        adjacentTrees += in.get(y - 1).get(x).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (y > 0 && x < in.get(y).size() - 1) {
                        adjacentTrees += in.get(y - 1).get(x + 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (x < in.get(y).size() - 1) {
                        adjacentTrees += in.get(y).get(x + 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (y < in.size() - 1 && x < in.get(y).size() - 1) {
                        adjacentTrees += in.get(y + 1).get(x + 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (y < in.size() - 1) {
                        adjacentTrees += in.get(y + 1).get(x).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (y < in.size() - 1 && x > 0) {
                        adjacentTrees += in.get(y + 1).get(x - 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (x > 0) {
                        adjacentTrees += in.get(y).get(x - 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (adjacentTrees >= 3) {
                        current.newState = SettlerTile.Type.FOREST;
                    }
                } else if (current.oldState == SettlerTile.Type.FOREST) {
                    int adjacentMills = 0;
                    if (x > 0 && y > 0) {
                        adjacentMills += in.get(y - 1).get(x - 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    }
                    if (y > 0) {
                        adjacentMills += in.get(y - 1).get(x).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    }
                    if (y > 0 && x < in.get(y).size() - 1) {
                        adjacentMills += in.get(y - 1).get(x + 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    }
                    if (x < in.get(y).size() - 1) {
                        adjacentMills += in.get(y).get(x + 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    }
                    if (y < in.size() - 1 && x < in.get(y).size() - 1) {
                        adjacentMills += in.get(y + 1).get(x + 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    }
                    if (y < in.size() - 1) {
                        adjacentMills += in.get(y + 1).get(x).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    }
                    if (y < in.size() - 1 && x > 0) {
                        adjacentMills += in.get(y + 1).get(x - 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    }
                    if (x > 0) {
                        adjacentMills += in.get(y).get(x - 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    }
                    if (adjacentMills >= 3) {
                        current.newState = SettlerTile.Type.LUMBERMILL;
                    }
                } else if (current.oldState == SettlerTile.Type.LUMBERMILL) {
                    int adjacentMills = 0;
                    int adjacentTrees = 0;
                    if (x > 0 && y > 0) {
                        adjacentMills += in.get(y - 1).get(x - 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                        adjacentTrees += in.get(y - 1).get(x - 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (y > 0) {
                        adjacentMills += in.get(y - 1).get(x).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                        adjacentTrees += in.get(y - 1).get(x).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (y > 0 && x < in.get(y).size() - 1) {
                        adjacentMills += in.get(y - 1).get(x + 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                        adjacentTrees += in.get(y - 1).get(x + 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (x < in.get(y).size() - 1) {
                        adjacentMills += in.get(y).get(x + 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                        adjacentTrees += in.get(y).get(x + 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (y < in.size() - 1 && x < in.get(y).size() - 1) {
                        adjacentMills += in.get(y + 1).get(x + 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                        adjacentTrees += in.get(y + 1).get(x + 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (y < in.size() - 1) {
                        adjacentMills += in.get(y + 1).get(x).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                        adjacentTrees += in.get(y + 1).get(x).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (y < in.size() - 1 && x > 0) {
                        adjacentMills += in.get(y + 1).get(x - 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                        adjacentTrees += in.get(y + 1).get(x - 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (x > 0) {
                        adjacentMills += in.get(y).get(x - 1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                        adjacentTrees += in.get(y).get(x - 1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                    }
                    if (adjacentMills >= 1 && adjacentTrees >= 1) {
                        current.newState = SettlerTile.Type.LUMBERMILL;
                    } else {
                        current.newState = SettlerTile.Type.OPEN;
                    }
                }

            }));
            in.forEach(l -> l.forEach(SettlerTile::oldToNew));
            this.minute++;
            long scoreOfMinute = getScoreOfMinute();
            if (this.scoreOfMiniute.values().contains(scoreOfMinute)) {
                Map<Integer, Long> matchingScore = this.scoreOfMiniute.entrySet().stream().filter(e -> e.getValue() == scoreOfMinute).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
                Set<Integer> integers = matchingScore.keySet();
                Optional<Integer> integerStream = integers.stream().filter(i -> IntStream.range(0, in.size()).allMatch(y -> IntStream.range(0, in.get(y).size()).allMatch(x -> this.oldStates.get(i).get(y).get(x).equals(in.get(y).get(x))))).findFirst();
                if (integerStream.isPresent()) {
                    this.firstCycleStart = integerStream.get();
                    this.hasBegunToCycle = true;
                    storeDeltasForCycle();
                }
            }
            this.oldStates.put(this.minute, in.stream().map(l -> l.stream().map(SettlerTile::clone).collect(Collectors.toList())).collect(Collectors.toList()));
            this.scoreOfMiniute.put(this.minute, scoreOfMinute);
        } else {
            int minutesLeft = targetMinute - this.minute;
            int minuteDelta = this.minute - this.firstCycleStart;
            int stepsOnLastCycle = minutesLeft % minuteDelta;
            this.scoreOfMiniute.put(targetMinute, this.scoreOfMiniute.get(this.firstCycleStart + stepsOnLastCycle));
            System.out.println(this.minute);
            this.minute = targetMinute;
            return;
        }

    }

    private void storeDeltasForCycle() {
        IntStream.rangeClosed(this.firstCycleStart + 1, minute)
                .forEach(i -> this.cycleScoreDeltas.put(i, this.scoreOfMiniute.get(i)));
    }

    long getScoreOfMinute() {
        Long lumberMills = in.stream().map(l -> l.stream().filter(t -> t.oldState == SettlerTile.Type.LUMBERMILL).count()).reduce(0L, Long::sum);
        Long forests = in.stream().map(l -> l.stream().filter(t -> t.oldState == SettlerTile.Type.FOREST).count()).reduce(0L, Long::sum);
        return lumberMills * forests;
    }

    public long getPart2() {
        tickMinutes(1000000000);
        return this.scoreOfMiniute.get(1000000000);
    }


    public List<List<SettlerTile>> readInput(Stream<String> stream) {
        return stream
                .map(s -> s.chars().mapToObj(SettlerTile::new).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public Day18() {
        System.out.println("::: Starting Day 18 :::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            in = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
