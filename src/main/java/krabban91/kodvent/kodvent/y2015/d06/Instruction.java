package krabban91.kodvent.kodvent.y2015.d06;

import java.awt.*;

public class Instruction {

    private Action action;
    private Point from;
    private Point to;

    public Instruction(String s) {
        int i = 1;
        String[] words = s.split(" ");
        if (s.contains("toggle")) {
            action = Action.TOGGLE;
        } else {
            i++;
            if (s.contains("off")) {
                action = Action.TURN_OFF;
            } else {
                action = Action.TURN_ON;
            }
        }
        String[] f = words[i].split(",");
        String[] t = words[i + 2].split(",");
        from = new Point(Integer.parseInt(f[0]), Integer.parseInt(f[1]));
        to = new Point(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
    }

    public Action getAction() {
        return action;
    }

    public Point getFrom() {
        return from;
    }

    public Point getTo() {
        return to;
    }
}
