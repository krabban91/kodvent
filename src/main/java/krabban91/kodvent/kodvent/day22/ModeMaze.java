package krabban91.kodvent.kodvent.day22;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModeMaze {

    int depth;
    Point target;
    Point entrance;

    Map<Point, Integer> dangerMap = new HashMap<>();
    Map<Point, Region> regionMap;

    public ModeMaze(List<String> input) {
        depth = Integer.parseInt(input.get(0).split(": ")[1]);
        String[] split = input.get(1).split(": ")[1].split(",");
        entrance = new Point(0, 0);
        target = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        regionMap = getRegionMap();
    }

    private Map<Point, Region> getRegionMap() {
        Map<Point, Integer> geoIndices = new HashMap<>();
        Map<Point, Integer> erosionLevels = new HashMap<>();
        return IntStream.rangeClosed(0, target.y + 100)
                .mapToObj(y -> IntStream.rangeClosed(0, target.x + 100)
                        .mapToObj(x -> {
                            int geoIndex = 0;
                            Point current = new Point(x, y);
                            if (current.x == entrance.x && current.y == entrance.y) {

                            } else if (current.x == target.x && current.y == target.y) {

                            } else if (current.y == entrance.y) {
                                geoIndex = current.x * 16807;
                            } else if (current.x == entrance.x) {
                                geoIndex = current.y * 48271;
                            } else {
                                geoIndex = erosionLevels.get(new Point(current.x - 1, current.y)) * erosionLevels.get(new Point(current.x, current.y - 1));
                            }
                            geoIndices.put(current, geoIndex);
                            int erosionLevel = (geoIndex + depth) % 20183;
                            erosionLevels.put(current, erosionLevel);
                            int danger = erosionLevel % 3;
                            this.dangerMap.put(current, danger);
                            switch (danger) {
                                case 0:
                                    return new Region(current, RegionType.ROCKY);
                                case 1:
                                    return new Region(current, RegionType.WET);
                                default:
                                    return new Region(current, RegionType.NARROW);
                            }
                        }).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toMap(r -> r.getPoint(), r -> r));
    }

    public long estimateDangerLevel() {
        return this.dangerMap.entrySet().stream()
                .filter(e -> e.getKey().x >= 0 && e.getKey().x <= target.x)
                .filter(e -> e.getKey().y >= 0 && e.getKey().y <= target.y)
                .map(e -> e.getValue()).reduce(0, Integer::sum);
    }

    public long fewestMinutesToReachTarget() {
        Map<Point, Set<Tool>> triedtools = new HashMap<>();
        Tool initiallyEquippedTool = Tool.TORCH;
        Region targetRegion = this.regionMap.get(target);

        Map<Point, TimeToRegion> checked = new HashMap<>();
        PriorityQueue<TimeToRegion> unchecked = new PriorityQueue<>(Comparator.comparingInt(TimeToRegion::getHeuristic));
        unchecked.add(new TimeToRegion(this.regionMap.get(entrance), 0, initiallyEquippedTool, targetRegion));
        while (!unchecked.isEmpty()) {
            TimeToRegion poll = unchecked.poll();
            Point current = poll.getRegionToReach().point;
            Tool equippedTool = poll.getEquippedTool();
            addToolAsTried(triedtools, current, equippedTool);
            checked.put(current, poll);
            if (poll.isTarget()) {
                break;
            }
            List<Point> explore = adjacentPoints(current);
            explore.forEach(point -> {
                Region region = regionMap.get(point);
                if (region != null) {
                    if (!triedtools.containsKey(region.point)) {
                        triedtools.put(region.point, new HashSet<>());
                    }
                    if (canUseTool(triedtools, poll, region, equippedTool)) {
                        addToUnchecked(targetRegion, unchecked, poll, equippedTool, region, 0);
                    } else {
                        Tool leftTool = rotateTool(equippedTool, -1);
                        if (canUseTool(triedtools, poll, region, leftTool)) {
                            addToUnchecked(targetRegion, unchecked, poll, leftTool, region, 7);
                        }
                        Tool rightTool = rotateTool(equippedTool, +1);
                        if (canUseTool(triedtools, poll, region, rightTool)) {
                            addToUnchecked(targetRegion, unchecked, poll, rightTool, region, 7);
                        }
                    }
                }
            });
        }
        return checked.get(target)
                .getElapsedTime();
    }

    private void addToolAsTried(Map<Point, Set<Tool>> triedtools, Point current, Tool equippedTool) {
        if (!triedtools.containsKey(current)) {
            triedtools.put(current, new HashSet<>());
        }
        Set<Tool> tried = triedtools.get(current);
        tried.add(equippedTool);
    }

    private void addToUnchecked(Region targetRegion, PriorityQueue<TimeToRegion> unchecked, TimeToRegion poll, Tool equippedTool, Region region, int extraCost) {
        int costToGoThere = isTargetButToolMismatch(equippedTool, region) ? 8 : 1;
        unchecked.add(new TimeToRegion(
                region,
                poll.getElapsedTime() + costToGoThere + extraCost,
                equippedTool,
                targetRegion));
    }

    private boolean isTargetButToolMismatch(Tool equippedTool, Region region) {
        return region.getPoint().equals(target) && !equippedTool.equals(Tool.TORCH);
    }

    private List<Point> adjacentPoints(Point current) {
        Point n = new Point(current.x, current.y - 1);
        Point w = new Point(current.x - 1, current.y);
        Point s = new Point(current.x, current.y + 1);
        Point e = new Point(current.x + 1, current.y);
        List<Point> explore = new ArrayList<>();
        Collections.addAll(explore, n, w, s, e);
        return explore;
    }

    private boolean canUseTool(Map<Point, Set<Tool>> triedtools, TimeToRegion poll, Region region, Tool leftTool) {
        return poll.getRegionToReach().canBeReachedWithTool(leftTool) && region.canBeReachedWithTool(leftTool) && !triedtools.get(region.point).contains(leftTool);
    }

    private static Tool rotateTool(Tool tool, int i) {
        if (i == 0) {
            return tool;
        } else if (i < 0) {
            return rotateTool(tool.equals(Tool.CLIMBING) ?
                    Tool.NEITHER :
                    (tool.equals(Tool.NEITHER) ?
                            Tool.TORCH : Tool.CLIMBING), i + 1);
        } else {
            return rotateTool(tool.equals(Tool.CLIMBING) ?
                    Tool.TORCH :
                    (tool.equals(Tool.NEITHER) ?
                            Tool.CLIMBING : Tool.NEITHER), i - 1);
        }
    }
}
