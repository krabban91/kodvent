package krabban91.kodvent.kodvent.y2018.d22;

import krabban91.kodvent.kodvent.y2018.d15.CaveBattle;

public class TimeToRegion {

    private Region regionToReach;
    private int elapsedTime;
    private Tool equippedTool;
    private Region target;

    public TimeToRegion(Region region, int elapsedTime,Tool equippedTool, Region target) {
        this.regionToReach = region;
        this.elapsedTime = elapsedTime;
        this.equippedTool = equippedTool;
        this.target = target;
    }

    public Region getRegionToReach() {
        return regionToReach;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }


    public int getHeuristic(){
        return getElapsedTime() + CaveBattle.manhattanDistance(regionToReach.point, target.point);
    }

    public Tool getEquippedTool() {
        return equippedTool;
    }

    public Region getTarget() {
        return target;
    }

    public boolean isTarget() {
        return this.regionToReach.point.equals(this.target.point);
    }
}
