package krabban91.kodvent.kodvent.y2016.d01;

import krabban91.kodvent.kodvent.utilities.Distances;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Taxi {
    private Point location = new Point(0, 0);
    private List<Point> movementVectors = Arrays.asList(
            new Point(0, -1),
            new Point(1, 0),
            new Point(0, 1),
            new Point(-1, 0));
    private int direction = 0;
    private List<Point> history = new ArrayList<>();
    private Point firstDouble;

    public void move(String instruction) {
        char c = instruction.charAt(0);
        direction = (4 + direction + (c == 'R' ? 1 : -1)) % 4;
        int distance = Integer.parseInt(instruction.substring(1));
        for (int i = 0; i < distance; i++) {
            Point vector = movementVectors.get(direction);
            location = new Point(location.x + vector.x, location.y + vector.y);
            if (firstDouble == null && history.contains(location)) {
                firstDouble = location;
            }
            history.add(location);
        }
    }

    public long distanceFromStart() {
        return Distances.manhattan(new Point(0, 0), location);
    }

    public long distanceFromStartToFirstDoubleVisit() {
        return Distances.manhattan(new Point(0, 0), firstDouble);
    }
}
