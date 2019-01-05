package krabban91.kodvent.kodvent.y2018.d22;

import krabban91.kodvent.kodvent.utilities.search.Graph;

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
    Map<Mode, Collection<Transition>> network;
    Map<Mode, TimeToMode> checked = new HashMap<>();

    private boolean debug;

    public ModeMaze(List<String> input) {
        depth = Integer.parseInt(input.get(0).split(": ")[1]);
        String[] split = input.get(1).split(": ")[1].split(",");
        entrance = new Point(0, 0);
        target = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        regionMap = getRegionMap();
        network = buildNetworkOfActions();
    }

    public long estimateDangerLevel() {
        return this.dangerMap.entrySet().stream()
                .filter(e -> e.getKey().x >= 0 && e.getKey().x <= target.x)
                .filter(e -> e.getKey().y >= 0 && e.getKey().y <= target.y)
                .map(Map.Entry::getValue)
                .reduce(0, Integer::sum);
    }

    public long fewestMinutesToReachTarget() {
        Region targetRegion = this.regionMap.get(target);

        PriorityQueue<TimeToMode> unchecked = new PriorityQueue<>(Comparator.comparingInt(TimeToMode::getHeuristic));
        unchecked.add(new TimeToMode(new Mode(this.regionMap.get(entrance), Tool.TORCH), new Mode(targetRegion, Tool.TORCH)));
        Graph<TimeToMode, Transition, Mode> graph = new Graph<>();
        TimeToMode search = graph.search(unchecked, this::nextAction, network);
        if (debug) {
            visualizePath(search);
        }
        return search.cost();
    }

    private Collection<TimeToMode> nextAction(TimeToMode previous, Map<Mode, Collection<Transition>> network) {
        if (checked.containsKey(previous.destination())) {
            return Collections.emptySet();
        }
        checked.put(previous.destination(), previous);
        return network.get(previous.destination()).stream()
                .map(a -> new TimeToMode(previous, a))
                .filter(ttr -> !previous.hasVisited(ttr.destination()))
                .filter(ttr -> !checked.containsKey(ttr.destination()))
                .collect(Collectors.toSet());
    }

    private Map<Point, Region> getRegionMap() {
        Map<Point, Integer> erosionLevels = new HashMap<>();
        return IntStream.rangeClosed(0, target.y + 20)
                .mapToObj(y -> IntStream.rangeClosed(0, target.x + 20)
                        .mapToObj(x -> {
                            int geoIndex;
                            Point current = new Point(x, y);
                            if (current.x == entrance.x && current.y == entrance.y) {
                                geoIndex = 0;
                            } else if (current.x == target.x && current.y == target.y) {
                                geoIndex = 0;
                            } else if (current.y == entrance.y) {
                                geoIndex = current.x * 16807;
                            } else if (current.x == entrance.x) {
                                geoIndex = current.y * 48271;
                            } else {
                                geoIndex = erosionLevels.get(new Point(current.x - 1, current.y)) * erosionLevels.get(new Point(current.x, current.y - 1));
                            }
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
                .collect(Collectors.toMap(Region::getPoint, r -> r));
    }

    private Map<Mode, Collection<Transition>> buildNetworkOfActions() {
        Collection<Tool> toolBox = new HashSet<>();
        toolBox.add(Tool.CLIMBING);
        toolBox.add(Tool.TORCH);
        toolBox.add(Tool.NEITHER);
        return regionMap.values().stream().map(e -> {
            Set<Region> neighborAreas = getNeighborRegions(e);
            Map<Mode, Collection<Transition>> surrounding = toolBox.stream()
                    .filter(e::canBeReachedWithTool)
                    .collect(Collectors.toMap(
                            t -> new Mode(e, t),
                            t -> neighborAreas.stream()
                                    .filter(r -> r.canBeReachedWithTool(t))
                                    .map(r -> new Mode(r, t))
                                    .map(tr -> new Transition(new Mode(e, tr.getTool()), tr, 1))
                                    .collect(Collectors.toSet())));
            Map<Mode, Transition> sameSlot = toolBox.stream()
                    .filter(e::canBeReachedWithTool)
                    .collect(Collectors.toMap(t -> new Mode(e, t), t -> new Transition(new Mode(e, t), new Mode(e, this.getOtherViableTool(e.getType(), t)), 7)));
            sameSlot.forEach((key, value) -> surrounding.get(key).add(value));
            return surrounding;
        }).map(Map::entrySet)
                .flatMap(Set::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Tool getOtherViableTool(RegionType regionType, Tool tool) {
        switch (regionType) {
            case WET:
                switch (tool) {
                    case CLIMBING:
                        return Tool.NEITHER;
                    case NEITHER:
                        return Tool.CLIMBING;
                    default:
                        return null;
                }
            case ROCKY:
                switch (tool) {
                    case CLIMBING:
                        return Tool.TORCH;
                    case NEITHER:
                        return null;
                    default:
                        return Tool.CLIMBING;
                }
            default:
                switch (tool) {
                    case CLIMBING:
                        return null;
                    case NEITHER:
                        return Tool.TORCH;
                    default:
                        return Tool.NEITHER;
                }
        }
    }

    private HashSet<Region> getNeighborRegions(Region region) {
        HashSet<Region> regions = new HashSet<>();
        Point p = (Point) region.getPoint().clone();
        p.translate(-1, 0);
        regions.add(regionMap.get(p));
        p.translate(+1, -1);
        regions.add(regionMap.get(p));
        p.translate(+1, +1);
        regions.add(regionMap.get(p));
        p.translate(-1, +1);
        regions.add(regionMap.get(p));
        regions.removeIf(Objects::isNull);
        return regions;
    }

    private void visualizePath(TimeToMode target) {
        Deque<TimeToMode> path = new ArrayDeque<>();
        TimeToMode current = target;
        while (current != null) {
            path.push(current);
            current = current.getPrevious();
        }
        while (!path.isEmpty()) {
            visualizeState(path.pop());
        }
    }

    private void visualizeState(TimeToMode currentLocation) {
        int minX = Math.max(currentLocation.destination().getRegion().getPoint().x - 20, regionMap.entrySet().stream().map(e -> e.getKey().x).min(Comparator.comparingInt(e -> e)).orElse(0));
        int maxX = Math.min(minX + 40, regionMap.entrySet().stream().map(e -> e.getKey().x).max(Comparator.comparingInt(e -> e)).orElse(0));
        int minY = Math.max(currentLocation.destination().getRegion().getPoint().y - 10, regionMap.entrySet().stream().map(e -> e.getKey().y).min(Comparator.comparingInt(e -> e)).orElse(0));
        int maxY = Math.min(minY + 20, regionMap.entrySet().stream().map(e -> e.getKey().y).max(Comparator.comparingInt(e -> e)).orElse(0));
        StringBuilder builder = new StringBuilder();
        String padding = "     ";

        builder.append('\n');
        StringBuilder yRow0 = new StringBuilder();
        yRow0.append(padding);
        IntStream.rangeClosed(minX, maxX).forEach(i -> yRow0.append((i / 100) > 0 ? i / 100 : " "));
        builder.append(yRow0.toString());
        builder.append('\n');
        StringBuilder yRow1 = new StringBuilder();
        yRow1.append(padding);
        IntStream.rangeClosed(minX, maxX).forEach(i -> yRow1.append((i / 10) > 0 ? (i / 10) % 10 : " "));
        builder.append(yRow1.toString());
        builder.append('\n');
        StringBuilder yRow2 = new StringBuilder();
        yRow2.append(padding);
        IntStream.rangeClosed(minX, maxX).forEach(i -> yRow2.append(i % 10));
        builder.append(yRow2.toString());
        builder.append('\n');
        IntStream.rangeClosed(minY, maxY).forEach(y -> {
            if ((y / 100) == 0) {
                builder.append("  ");
            }
            if ((y / 10) == 0) {
                builder.append(" ");
            }
            builder.append((y));
            builder.append(" ");
            IntStream.rangeClosed(minX, maxX).forEach(x -> visualizeCell(currentLocation, builder, y, x));
            builder.append('\n');
        });
        System.out.println(builder.toString());
    }

    private void visualizeCell(TimeToMode currentLocation, StringBuilder builder, int y, int x) {
        Point current = new Point(x, y);
        if (current.equals(currentLocation.destination().getRegion().getPoint())) {
            builder.append('X');
        } else if (current.equals(entrance)) {
            builder.append('M');
        } else if (current.equals(target)) {
            builder.append('T');
        } else {
            Region region = regionMap.get(current);
            switch (region.getType()) {
                case NARROW:
                    builder.append('|');
                    break;
                case WET:
                    builder.append('=');
                    break;
                default:
                    builder.append('.');
            }
        }
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
