package krabban91.kodvent.kodvent.day3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Claim {

    private static Pattern pattern = Pattern.compile("[( @ )(#)(,)(: )(x)]");
    private int id;
    private int x;
    private int y;
    private int width;
    private int height;

    public Claim(String claim) {
        List<String> strings = new ArrayList<>(Arrays.asList(pattern.split(claim)));
        strings.removeIf(s -> s.equals(""));
        this.id = Integer.parseInt(strings.get(0));
        this.x = Integer.parseInt(strings.get(1));
        this.y = Integer.parseInt(strings.get(2));
        this.width = Integer.parseInt(strings.get(3));
        this.height = Integer.parseInt(strings.get(4));
    }

    public int getId() {
        return id;
    }

    public int getX0() {
        return x;
    }

    public int getY0() {
        return y;
    }

    public int getX1() {
        return x + width;
    }

    public int getY1() {
        return y + height;
    }
}
