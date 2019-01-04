package krabban91.kodvent.kodvent.y2018.d15;

import krabban91.kodvent.kodvent.utilities.Distances;
import krabban91.kodvent.kodvent.utilities.search.Graph;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CaveBattle {

    List<List<Boolean>> map;
    List<BattleUnit> units = new ArrayList();
    Set<Point> caveArea;
    Map<Point, Collection<Step>> network;
    Map<Point, Map<Point, Map<Point, DistanceToPoint>>> knownDistances;
    long initialElfCount;
    int round = 0;
    private boolean battleIsOver = false;
    private boolean debug;

    public CaveBattle(List<String> rows) {
        map = rows.stream().map(this::getMapRow).collect(Collectors.toList());
        caveArea = IntStream.range(0, map.size()).mapToObj(y ->
                IntStream.range(0, map.get(y).size())
                        .filter(x -> map.get(y).get(x))
                        .mapToObj(x -> new Point(x, y))
                        .collect(Collectors.toSet()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        regenerateNetwork();
        IntStream.range(0, rows.size()).forEach(y -> IntStream.range(0, rows.get(y).length())
                .filter(x -> rows.get(y).charAt(x) == 'G' || rows.get(y).charAt(x) == 'E')
                .forEach(x -> units.add(new BattleUnit(
                        new Point(x, y),
                        rows.get(y).charAt(x) == 'G',
                        200,
                        this))));
        initialElfCount = countElfs();
    }

    public void regenerateNetwork() {
        network = caveArea.stream().collect(Collectors.toMap(
                e -> e,
                e -> caveArea.stream()
                        .filter(p -> Distances.manhattan(e, p) == 1)
                        .filter(this::tileIsEmpty)
                        .map(p -> new Step(e, p))
                        .collect(Collectors.toList())));
        knownDistances = new HashMap<>();
    }

    public void setElfStrength(int strength) {
        units.stream().filter(u -> !u.isGoblin()).forEach(u -> u.setAttackPower(strength));
    }

    public long countElfs() {
        return units.stream().filter(u -> !u.isGoblin()).count();
    }

    private List<Boolean> getMapRow(String row) {
        return row.chars().mapToObj(c -> (char) c == '.' || (char) c == 'E' || (char) c == 'G').collect(Collectors.toList());
    }

    public long battleUntilItIsOverOrAnElfDies() {
        while (true) {
            if(debug){
                visualizeBattle();
            }
            units.stream()
                    .sorted(this::order)
                    .forEach(BattleUnit::movaAndAttack);
            units = units.stream().filter(BattleUnit::isAlive).collect(Collectors.toList());
            if (battleIsOver() || initialElfCount != countElfs()) {
                visualizeBattle();
                return getBattleScore();
            }
            round++;
        }
    }

    public long battleUntilItIsOver() {
        while (true) {
            visualizeBattle();
            units.stream()
                    .sorted(this::order)
                    .forEach(BattleUnit::movaAndAttack);
            units = units.stream().filter(BattleUnit::isAlive).collect(Collectors.toList());
            if (battleIsOver()) {
                visualizeBattle();
                return getBattleScore();
            }
            round++;
        }
    }

    public int order(BattleUnit l, BattleUnit r) {
        int compareY = Integer.compare(l.getLocation().y, r.getLocation().y);
        if (compareY == 0) {
            return Double.compare(l.getLocation().x, r.getLocation().x);
        }
        return compareY;
    }

    private long getBattleScore() {
        return (long) round * units.stream().map(BattleUnit::getHitpoints).reduce(0, Integer::sum);
    }

    public Stream<BattleUnit> targets(boolean isGoblin) {
        return units.stream()
                .filter(BattleUnit::isAlive)
                .filter(u -> u.isGoblin() == isGoblin);
    }

    public Stream<BattleUnit> targetsNextToPoint(boolean isGoblin, Point point) {
        return targets(isGoblin)
                .filter(unit -> unit.isNextTo(point));
    }

    public boolean battleIsOver() {
        return this.battleIsOver;
    }

    public void endBattle() {
        this.battleIsOver = true;
    }

    public boolean removeFromBattle(BattleUnit battleUnit) {
        return this.units.remove(battleUnit);
    }

    public boolean canReach(
            BattleUnit battleUnit, Point point) {
        return distanceBetween(battleUnit.location, point) != null;
    }

    public List<Point> avaliableStrikingLocations(BattleUnit battleUnit) {
        ArrayList<Point> points = new ArrayList<>();
        int x = battleUnit.location.x;
        int y = battleUnit.location.y;

        points.add(new Point(x, y - 1));
        points.add(new Point(x - 1, y));
        points.add(new Point(x + 1, y));
        points.add(new Point(x, y + 1));
        return points.stream().filter(this::tileIsEmpty).collect(Collectors.toList());
    }

    private boolean tileIsEmpty(Point point) {
        return point.x >= 0 && point.y >= 0 &&
                caveArea.contains(point) &&
                units.stream()
                        .noneMatch(unit -> unit.getLocation().x == point.x && unit.getLocation().y == point.y);
    }

    public DistanceToPoint distanceBetween(
            Point from, Point to) {
        if (!knownDistances.containsKey(from)) {
            knownDistances.put(from, new HashMap<>());
        }
        Map<Point, Map<Point, DistanceToPoint>> fromMap = knownDistances.get(from);
        if(!fromMap.containsKey(to)){
            fromMap.put(to, new HashMap<>());
        }
        Map<Point, DistanceToPoint> checked = fromMap.get(to);
        PriorityQueue<DistanceToPoint> unChecked = newQueue(from, to);
        if (!checked.containsKey(to)) {
            Graph<DistanceToPoint, Step, Point> graph = new Graph<>();
            checked.put(to, graph.search(unChecked, this::addNext, network));
        }
        return checked.get(to);
    }

    private Collection<DistanceToPoint> addNext(DistanceToPoint distanceToPoint, Map<Point, Collection<Step>> network) {
        return network.get(distanceToPoint.destination())
                .stream()
                .map(w -> new DistanceToPoint(distanceToPoint, w))
                .filter(d -> this.tileIsEmpty(d.destination()))
                .filter(d -> !distanceToPoint.hasVisited(d.destination()))
                .collect(Collectors.toSet());
    }

    private PriorityQueue<DistanceToPoint> newQueue(Point from, Point to) {
        PriorityQueue<DistanceToPoint> queue = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint::heuristic));
        queue.add(new DistanceToPoint(from, to));
        return queue;
    }

    public void setDebug(boolean debug){
        this.debug = debug;
        units.forEach(u -> u.setDebug(debug));
    }

    private void visualizeBattle() {
        StringBuilder builder = new StringBuilder();
        builder.append(": Round " + round + "! :");
        builder.append('\n');
        visualizeMap(builder);
        System.out.println(builder.toString());
    }

    private void visualizeMap(StringBuilder builder) {
        StringBuilder yRow1 = new StringBuilder();
        yRow1.append("   ");
        IntStream.range(0, map.get(0).size()).forEach(i -> yRow1.append((i / 10) > 0 ? i / 10 : " "));
        builder.append(yRow1.toString());
        builder.append('\n');
        StringBuilder yRow2 = new StringBuilder();
        yRow2.append("   ");
        IntStream.range(0, map.get(0).size()).forEach(i -> yRow2.append(i % 10));
        builder.append(yRow2.toString());
        builder.append('\n');
        IntStream.range(0, map.size()).forEach(y -> {
            visualizeMapRow(builder, y);
        });
    }

    private void visualizeMapRow(StringBuilder builder, int y) {
        int rowSize = map.get(y).size();
        if ((y / 10) == 0) {
            builder.append(" ");
        }
        builder.append(y);
        builder.append(" ");
        IntStream.range(0, rowSize).forEach(x -> visualizeMapCell(builder, y, x));
        builder.append(" ");
        units.stream()
                .filter(u -> u.location.y == y)
                .sorted(Comparator.comparingInt(u -> u.getLocation().x))
                .forEach(u -> {
                    builder.append(u.toString());
                });

        builder.append('\n');
    }

    private void visualizeMapCell(StringBuilder builder, int y, int x) {
        if (map.get(y).get(x)) {
            Optional<BattleUnit> any = units.stream().filter(u -> u.location.x == x && u.location.y == y).findAny();
            if (any.isPresent()) {
                if (any.get().isGoblin()) {
                    builder.append('G');
                } else {
                    builder.append('E');
                }
            } else {
                builder.append('.');
            }
        } else {
            builder.append('#');
        }
    }
}
