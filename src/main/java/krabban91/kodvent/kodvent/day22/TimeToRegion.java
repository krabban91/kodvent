package krabban91.kodvent.kodvent.day22;

import krabban91.kodvent.kodvent.day15.CaveBattle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
