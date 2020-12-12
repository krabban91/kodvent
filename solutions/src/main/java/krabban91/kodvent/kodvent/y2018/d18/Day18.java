package krabban91.kodvent.kodvent.y2018.d18;

import krabban91.kodvent.kodvent.utilities.Grid;
import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day18 {
    Grid<SettlerTile> in;
    boolean hasBegunToCycle;
    Integer firstCycleStart;

    private Map<Integer, Grid<SettlerTile>> oldStates = new HashMap<>();
    private Map<Integer, Long> scoreOfMiniute = new HashMap<>();
    private Map<Integer, Long> cycleScoreDeltas = new HashMap<>();
    private int minute = 0;
    private boolean debug;

    public Day18() {
        System.out.println("::: Starting Day 18 :::");
        String inputPath = "y2018/d18/input.txt";
        readInput(inputPath);

        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

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
        System.out.println(LogUtils.tiles(in.getRaw()));
    }

    private Grid<SettlerTile> clonedState() {
        return in.clone(SettlerTile::clone);
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
        this.in.forEachRanged((x, y) -> {
            SettlerTile current = in.get(x, y);
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
        });
        in.forEach(SettlerTile::oldToNew);
    }

    private List<SettlerTile> getAdjacentTiles(Grid<SettlerTile> in, int currX, int currY) {
        return in.getSurroundingTiles(currX, currY);
    }

    private void storeDeltasForCycle() {
        IntStream.rangeClosed(this.firstCycleStart + 1, minute)
                .forEach(i -> this.cycleScoreDeltas.put(i, this.scoreOfMiniute.get(i)));
    }

    long getScoreOfMinute() {
        Long lumberMills = in.sum(t -> t.isLumberMill() ? 1 : 0);
        Long forests = in.sum(t -> t.isForest() ? 1 : 0);
        return lumberMills * forests;
    }

    public void readInput(String path) {
        this.in = new Grid<>(Input.getLines(path).stream()
                .map(s -> s.chars()
                        .mapToObj(SettlerTile::new)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }
}
