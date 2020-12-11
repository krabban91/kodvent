package krabban91.kodvent.kodvent.y2015.d18;

import krabban91.kodvent.kodvent.utilities.Grid;
import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

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

    public long runGIF(Grid<Light> lightGrid, int seconds, boolean forcedCorners) {
        List<Point> corners = List.of(
                new Point(0, 0),
                new Point(lightGrid.width() - 1,0),
                new Point(0,lightGrid.height() - 1),
                new Point(lightGrid.width() - 1,lightGrid.height() - 1));

        if (debug) {
            System.out.println("Initial state");
            System.out.println(LogUtils.tiles(lightGrid));
        }
        for (int s = 1; s <= seconds; s++) {
            lightGrid.forEach((l,p) -> {
                if(forcedCorners && corners.contains(p)){
                    l.setNextState(true);
                } else{
                    l.setNextState(lightGrid.getSurroundingTiles(p.y, p.x));
                }
            });
            lightGrid.forEach(Light::moveForwardInTime);
            if (debug) {
                System.out.println("state after second " + s);
                System.out.println(LogUtils.tiles(lightGrid));
            }
        }
        return lightGrid.sum(Light::getState);
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath);
    }

    public Grid<Light> getNewLights() {
        return new Grid<>(in.stream().map(s -> s.chars()
                .mapToObj(Light::new)
                .collect(Collectors.toList()))
                .collect(Collectors.toList()));
    }
}
