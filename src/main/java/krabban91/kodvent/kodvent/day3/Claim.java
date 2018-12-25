package krabban91.kodvent.kodvent.day3;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Claim {

    private static Pattern pattern = Pattern.compile("[( @ )(#)(,)(: )(x)]");
    private int id;
    private Point point;
    private Point size;

    public Claim(String claim) {
        List<String> strings = new ArrayList<>(Arrays.asList(pattern.split(claim)));
        strings.removeIf(s -> s.equals(""));
        this.id = Integer.parseInt(strings.get(0));
        this.point = new Point(Integer.parseInt(strings.get(1)),
        Integer.parseInt(strings.get(2)));
        this.size = new Point(Integer.parseInt(strings.get(3)),

        Integer.parseInt(strings.get(4)));
    }

    public int getId() {
        return id;
    }

    public int getX0() {
        return point.x;
    }

    public int getY0() {
        return point.y;
    }

    public int getX1() {
        return point.x + size.x;
    }

    public int getY1() {
        return point.y + size.y;
    }
}
