package krabban91.kodvent.kodvent.y2019.d03;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WirePath {
    char direction;
    int distance;
    Point vector;
    List<Point> pointsInPath;

    public WirePath(String s) {
        direction = s.charAt(0);
        distance = Integer.parseInt(s.substring(1));
        pointsInPath = new ArrayList<>();
        if (direction == 'R') {
            vector = new Point(distance,0);
            pointsInPath = IntStream.rangeClosed(1, distance)
                    .mapToObj(i -> new Point(i, 0)).collect(Collectors.toList());
        } else if (direction == 'L') {
            vector = new Point(-distance,0);
            pointsInPath = IntStream.rangeClosed(1, distance)
                    .mapToObj(i -> new Point(-i, 0)).collect(Collectors.toList());

        } else if (direction == 'D') {
            vector = new Point(0,distance);

            pointsInPath = IntStream.rangeClosed(1, distance)
                    .mapToObj(i -> new Point(0, i)).collect(Collectors.toList());

        } else if (direction == 'U') {
            vector = new Point(0,-distance);
            pointsInPath = IntStream.rangeClosed(1, distance)
                    .mapToObj(i -> new Point(0, -i)).collect(Collectors.toList());
        } else {
            throw new RuntimeException();
        }
    }
}
