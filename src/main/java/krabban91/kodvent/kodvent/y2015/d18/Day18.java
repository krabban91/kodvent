package krabban91.kodvent.kodvent.y2015.d18;

import krabban91.kodvent.kodvent.utilities.Grid;
import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Day18 {
    List<String> in;
    boolean debug = false;

    public Day18() {
        System.out.println("::: Starting Day 18 :::");
        String inputPath = "y2015/d18/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        return runGIF(getNewLights(), 100, false);
    }

    public long getPart2() {
        return runGIF(getNewLights(), 100, true);
    }

    public long runGIF(List<List<Light>> lights, int seconds, boolean forcedCorners) {
        Grid<Light> lightGrid = new Grid<>();
        if (debug) {
            System.out.println("Initial state");
            System.out.println(LogUtils.tiles(lights));
        }
        for (int s = 1; s <= seconds; s++) {
            lightGrid.forEachRanged(lights, (row, col) ->
                    lights.get(row).get(col).setNextState(lightGrid.getSurroundingTiles(row, col, lights)));
            if (forcedCorners) {
                lights.get(0).get(0).setNextState(true);
                lights.get(0).get(lights.get(0).size() - 1).setNextState(true);
                lights.get(lights.size() - 1).get(0).setNextState(true);
                lights.get(lights.size() - 1).get(lights.get(0).size() - 1).setNextState(true);
            }

            lightGrid.forEachRanged(lights, (row, col) -> lights.get(row).get(col).moveForwardInTime());
            if (debug) {
                System.out.println("state after second " + s);
                System.out.println(LogUtils.tiles(lights));
            }
        }
        return lights.stream().mapToInt(r -> r.stream().mapToInt(Light::getState).sum()).sum();
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath);
    }

    public List<List<Light>> getNewLights() {
        return in.stream().map(s -> s.chars()
                .mapToObj(Light::new)
                .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
