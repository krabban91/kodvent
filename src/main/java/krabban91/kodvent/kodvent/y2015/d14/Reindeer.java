package krabban91.kodvent.kodvent.y2015.d14;

public class Reindeer {
    private final String name;
    private final int requiredRest;
    private final long velocity;
    private final int dashtime;
    private int restingLeft;
    private int dashingLeft;
    private long distance;
    private int score;

    public Reindeer(String instructions) {
        String[] s = instructions.split(" ");
        name = s[0];
        requiredRest = Integer.parseInt(s[13]);
        velocity = Long.parseLong(s[3]);
        dashtime = Integer.parseInt(s[6]);
        dashingLeft = dashtime;
    }

    public void move() {
        if (restingLeft > 0) {
            restingLeft--;
            if (restingLeft == 0) {
                dashingLeft = dashtime;
            }
        } else {
            dashingLeft--;
            distance += velocity;
            if (dashingLeft == 0) {
                restingLeft = requiredRest;
            }
        }
    }
    public void score(){
        score++;
    }

    public int getScore() {
        return score;
    }

    public long getDistance() {
        return distance;
    }
}
