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

    private Map<Integer, List<List<SettlerTile>>> oldStates = new HashMap<>();
    private Map<Integer, Long> scoreOfMiniute = new HashMap<>();
    private Map<Integer, Long> cycleScoreDeltas = new HashMap<>();
    private int minute = 0;
    private boolean debug;

    public long getPart1() {
        tickMinutes(10);
        return this.scoreOfMiniute.get(10);
    }

    public long getPart2() {
        tickMinutes(1000000000);
        return this.scoreOfMiniute.get(1000000000);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    void tickMinutes(int minute) {
        while (this.minute < minute) {
            if (debug) {
                visualizeLand();
            }
            tickOneMinute(minute);
        }
    }

    void tickOneMinute(int targetMinute) {
        if (!hasBegunToCycle || targetMinute < firstCycleStart) {
            updateTilesOneMinute();
            this.minute++;
            checkIfCycleHasStarted();
            this.oldStates.put(this.minute, clonedState());
            this.scoreOfMiniute.put(this.minute, getScoreOfMinute());
        } else {
            calculateAndPutScoreForCyclicValue(targetMinute);
            this.minute = targetMinute;
            return;
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

    private List<List<SettlerTile>> clonedState() {
        return in.stream()
                .map(l -> l.stream()
                        .map(SettlerTile::clone)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private void checkIfCycleHasStarted() {
        long scoreOfMinute = getScoreOfMinute();
        if (!hasBegunToCycle && this.scoreOfMiniute.values().contains(scoreOfMinute)) {
            Set<Integer> matchingRounds = this.scoreOfMiniute.entrySet().stream().filter(e -> e.getValue() == scoreOfMinute).map(e -> e.getKey()).collect(Collectors.toSet());
            Optional<Integer> firstCycle = matchingRounds.stream()
                    .filter(i -> this.oldStates.get(i).equals(this.in)).findFirst();
            if (firstCycle.isPresent()) {
                this.firstCycleStart = firstCycle.get();
                this.hasBegunToCycle = true;
                storeDeltasForCycle();
            }
        }
    }

    private void calculateAndPutScoreForCyclicValue(int targetMinute) {
        int minutesLeft = targetMinute - this.minute;
        int minuteDelta = this.minute - this.firstCycleStart;
        int stepsOnLastCycle = minutesLeft % minuteDelta;
        this.scoreOfMiniute.put(targetMinute, this.scoreOfMiniute.get(this.firstCycleStart + stepsOnLastCycle));
    }

    private void updateTilesOneMinute() {
        IntStream.range(0, in.size()).forEach(y -> IntStream.range(0, in.get(y).size()).forEach(x -> {
            SettlerTile current = in.get(y).get(x);
            List<SettlerTile> adjacentTiles = getAdjacentTiles(in, x, y);
            long adjacentForest = adjacentTiles.stream().filter(SettlerTile::isForest).count();
            long adjacentMill = adjacentTiles.stream().filter(SettlerTile::isLumberMill).count();
            if (current.isOpen() && adjacentForest >= 3) {
                current.newState = SettlerTile.Type.FOREST;
            } else if (current.isForest() && adjacentMill >= 3) {
                current.newState = SettlerTile.Type.LUMBERMILL;
            } else if (current.isLumberMill() && !(adjacentMill >= 1 && adjacentForest >= 1)) {
                current.newState = SettlerTile.Type.OPEN;
            }
        }));
        in.forEach(l -> l.forEach(SettlerTile::oldToNew));
    }

    private List<SettlerTile> getAdjacentTiles(List<List<SettlerTile>> in, int currX, int currY) {
        return IntStream.range(0, in.size())
                .filter(y -> Math.abs(y - currY) <= 1)
                .mapToObj(y -> IntStream.range(0, in.get(y).size())
                        .filter(x -> Math.abs(x - currX) <= 1)
                        .filter(x -> !(x == currX && y == currY))
                        .mapToObj(x -> in.get(y).get(x))
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private void storeDeltasForCycle() {
        IntStream.rangeClosed(this.firstCycleStart + 1, minute)
                .forEach(i -> this.cycleScoreDeltas.put(i, this.scoreOfMiniute.get(i)));
    }

    long getScoreOfMinute() {
        Long lumberMills = in.stream().map(l -> l.stream().filter(SettlerTile::isLumberMill).count()).reduce(0L, Long::sum);
        Long forests = in.stream().map(l -> l.stream().filter(SettlerTile::isForest).count()).reduce(0L, Long::sum);
        return lumberMills * forests;
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
