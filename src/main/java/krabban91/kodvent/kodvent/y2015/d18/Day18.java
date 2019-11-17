package krabban91.kodvent.kodvent.y2015.d18;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
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
        if (debug) {
            System.out.println("Initial state");
            System.out.println(LogUtils.tiles(lights));
        }
        for (int s = 1; s <= seconds; s++) {
            IntStream.range(0, lights.size())
                    .forEach(row -> IntStream.range(0, lights.get(row).size())
                            .forEach(col -> {
                                List<Light> collect = getSurroundingLights(row, col, lights);
                                lights.get(row).get(col).setNextState(collect);
                            }));
            if (forcedCorners) {
                lights.get(0).get(0).setNextState(true);
                lights.get(0).get(lights.get(0).size() - 1).setNextState(true);
                lights.get(lights.size() - 1).get(0).setNextState(true);
                lights.get(lights.size() - 1).get(lights.get(0).size() - 1).setNextState(true);
            }
            IntStream.range(0, lights.size()).forEach(row -> IntStream.range(0, lights.get(row).size()).forEach(col -> {
                lights.get(row).get(col).moveForwardInTime();
            }));
            if (debug) {
                System.out.println("state after second " + s);
                System.out.println(LogUtils.tiles(lights));
            }
        }
        return lights.stream().mapToInt(r -> r.stream().mapToInt(Light::getState).sum()).sum();
    }


    private List<Light> getSurroundingLights(int row, int col, List<List<Light>> lights) {
        return IntStream
                .rangeClosed(Math.max(row - 1, 0), Math.min(row + 1, lights.size() - 1))
                .mapToObj(i -> IntStream
                        .rangeClosed(Math.max(col - 1, 0), Math.min(col + 1, lights.get(row).size() - 1))
                        .mapToObj(j -> (i == row && j == col) ? null : lights.get(i).get(j))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
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
