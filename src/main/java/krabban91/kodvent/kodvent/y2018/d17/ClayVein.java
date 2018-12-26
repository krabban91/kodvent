package krabban91.kodvent.kodvent.y2018.d17;

import java.awt.Point;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClayVein {

    List<Point> deposits;

    public ClayVein(String input){
        String[] split = input.split("=");
        String direction = split[0];
        int single = Integer.parseInt(split[1].split(",")[0]);
        String[] range = split[2].split("\\.\\.");
        int r0 = Integer.parseInt(range[0]);
        int r1 = Integer.parseInt(range[1]);
        deposits = IntStream.rangeClosed(r0,r1)
                .mapToObj(i -> direction.equals("x") ?
                        new Point(single, i) :
                        new Point(i, single))
                .collect(Collectors.toList());
    }

    public List<Point> getDeposits() {
        return deposits;
    }
}
