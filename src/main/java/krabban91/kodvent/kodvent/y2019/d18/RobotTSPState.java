package krabban91.kodvent.kodvent.y2019.d18;

import krabban91.kodvent.kodvent.y2018.d15.DistanceToPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotTSPState {

    private final Point center;
    private final TSPState tl;
    private final TSPState tr;
    private final TSPState bl;
    private final TSPState br;

    public RobotTSPState(Point center, TSPState tl, TSPState tr, TSPState bl, TSPState br) {
        this.center = center;
        this.tl = tl;
        this.tr = tr;
        this.bl = bl;
        this.br = br;
    }

    public static boolean isSameState(RobotTSPState one, RobotTSPState another) {
        return TSPState.isSameState(one.bl, another.bl) &&
                TSPState.isSameState(one.br, another.br) &&
                TSPState.isSameState(one.tl, another.tl) &&
                TSPState.isSameState(one.tr, another.tr);
    }

    public RobotTSPState copy() {
        return new RobotTSPState(this.center, tl.copy(), tr.copy(), bl.copy(), br.copy());
    }

    public int cost() {
        return tl.cost() + tr.cost() + bl.cost() + br.cost();
    }

    public void walkTo(DistanceToPoint next) {
        int key;
        if (next.destination().y > center.y) {
            if (next.destination().x > center.x) {
                key = br.walkTo(next);
            } else {
                key = bl.walkTo(next);
            }
        } else {
            if (next.destination().x > center.x) {
                key = tr.walkTo(next);
            } else {
                key = tl.walkTo(next);
            }
        }
        bl.unlockKey(key);
        br.unlockKey(key);
        tl.unlockKey(key);
        tr.unlockKey(key);
    }

    public Map<Point, List<Point>> targets(Map<Integer, Point> keyLookup) {
        Map<Point, List<Point>> targets = new HashMap<>();
        targets.put(tl.currentLocation(), tl.targets(keyLookup));
        targets.put(tr.currentLocation(), tr.targets(keyLookup));
        targets.put(bl.currentLocation(), bl.targets(keyLookup));
        targets.put(br.currentLocation(), br.targets(keyLookup));
        return targets;

    }

    public List<Integer> getKey() {
        List<Integer> keys = new ArrayList<>();
        keys.addAll(tl.getKey());
        keys.addAll(tr.getKey());
        keys.addAll(bl.getKey());
        keys.addAll(br.getKey());
        return keys;
    }

    public Map<Point, Integer> getKeys() {
        HashMap<Point, Integer> keysLeft = new HashMap<>();
        keysLeft.putAll(bl.getKeys());
        keysLeft.putAll(br.getKeys());
        keysLeft.putAll(tl.getKeys());
        keysLeft.putAll(tr.getKeys());
        return keysLeft;
    }
}
