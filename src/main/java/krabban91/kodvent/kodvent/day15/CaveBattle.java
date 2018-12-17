package krabban91.kodvent.kodvent.day15;


import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CaveBattle {

    public static final int UNREACHABLE_DISTANCE = 10000;
    List<List<Boolean>> map;
    List<BattleUnit> units = new ArrayList();
    long initialElfCount;
    int round = 0;
    private boolean battleIsOver = false;

    public CaveBattle(List<String> rows) {
        map = rows.stream().map(this::getMapRow).collect(Collectors.toList());
        IntStream.range(0, rows.size()).forEach(y -> IntStream.range(0, rows.get(y).length())
                .filter(x -> rows.get(y).charAt(x) == 'G' || rows.get(y).charAt(x) == 'E')
                .forEach(x -> units.add(new BattleUnit(
                        new Point(x, y),
                        rows.get(y).charAt(x) == 'G',
                        200,
                        this))));
        initialElfCount = countElfs();
    }

    public void setElfStrength(int strength){
        units.stream().filter(u -> !u.isGoblin()).forEach(u -> u.setAttackPower(strength));
    }

    public long countElfs() {
        return units.stream().filter(u -> !u.isGoblin()).count();
    }

    private void visualizeBattle() {
        System.out.println(": Round " + round + "! :");
        StringBuilder yRow1 = new StringBuilder();
        yRow1.append("   ");
        IntStream.range(0, map.get(0).size()).forEach(i -> yRow1.append((i / 10) > 0 ? i / 10 : " "));
        System.out.println(yRow1.toString());
        StringBuilder yRow2 = new StringBuilder();
        yRow2.append("   ");
        IntStream.range(0, map.get(0).size()).forEach(i -> yRow2.append(i % 10));
        System.out.println(yRow2.toString());
        IntStream.range(0, map.size()).forEach(y -> {
            StringBuilder builder = new StringBuilder();
            int rowSize = map.get(y).size();
            if ((y / 10) == 0) {
                builder.append(" ");
            }
            builder.append((y));
            builder.append(" ");
            IntStream.range(0, rowSize).forEach(x -> {
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
            });
            builder.append(" ");
            units.stream()
                    .filter(u -> u.location.y == y)
                    .sorted(Comparator.comparingInt(u -> u.getLocation().x))
                    .forEach(u -> {
                        builder.append(u.toString());
                    });

            System.out.println(builder.toString());
        });
    }

    List<Boolean> getMapRow(String row) {
        return row.chars().mapToObj(c -> (char) c == '.' || (char) c == 'E' || (char) c == 'G').collect(Collectors.toList());
    }

    public long battleUntilItIsOverOrAnElfDies() {
        while (true) {
            visualizeBattle();
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
        return round * units.stream().map(BattleUnit::getHitpoints).reduce(0, Integer::sum);
    }

    public Stream<BattleUnit> targets(boolean isGoblin) {
        return units.stream()
                .filter(BattleUnit::isAlive)
                .filter(u -> u.isGoblin() == isGoblin);
    }

    public Stream<BattleUnit> targetsNextToPoint(boolean isGoblin, Point point) {
        return targets(isGoblin)
                .filter(unit -> manhattanDistance(unit.getLocation(), point) == 1);
    }

    public static int manhattanDistance(Point point, Point other) {
        return (int) (Math.abs(point.getX() - other.getX()) + Math.abs(point.getY() - other.getY()));
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

    public boolean canReach(BattleUnit battleUnit, Point point) {
        DistanceToPoint distanceToPoint = distanceBetween(battleUnit.location, point);
        return distanceToPoint != null && distanceToPoint.distance > 0 && distanceToPoint.distance < CaveBattle.UNREACHABLE_DISTANCE;
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
        return point.x >= 0 && point.y >= 0 && map.get(point.y).get(point.x) &&
                !units.stream()
                        .anyMatch(unit -> unit.getLocation().x == point.x && unit.getLocation().y == point.y);
    }


    public DistanceToPoint distanceBetween(Point from, Point to) {
        Map<Point, DistanceToPoint> checked = new HashMap<>();
        PriorityQueue<DistanceToPoint> unChecked = newQueue(from, to);
        if (!(checked.containsKey(to))) {
            while (!unChecked.isEmpty()) {
                DistanceToPoint poll = unChecked.poll();
                addPointToChecked(checked, unChecked, poll);
                if (poll.point.equals(to)) {
                    return poll;
                }
            }
        }
        return checked.get(to);
    }

    private PriorityQueue<DistanceToPoint> newQueue(Point from, Point to) {
        PriorityQueue<DistanceToPoint> queue = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint::heuristic));
        queue.add(new DistanceToPoint(from, 0, to));
        return queue;
    }

    private void addPointToChecked(Map<Point, DistanceToPoint> checked, PriorityQueue<DistanceToPoint> unChecked, DistanceToPoint poll) {
        checked.put(poll.point, poll);
        addSuroundingPointsToUnchecked(poll, checked, unChecked);
    }

    private void addSuroundingPointsToUnchecked(DistanceToPoint poll, Map<Point, DistanceToPoint> checked, PriorityQueue<DistanceToPoint> unChecked) {
        int x = poll.point.x;
        int y = poll.point.y;
        Point point1 = new Point(x, y - 1);
        if (!checked.containsKey(point1) && tileIsEmpty(point1)) {
            unChecked.add(new DistanceToPoint(point1, poll.distance + 1, poll.target));
        }
        point1 = new Point(x - 1, y);
        if (!checked.containsKey(point1) && tileIsEmpty(point1)) {
            unChecked.add(new DistanceToPoint(point1, poll.distance + 1, poll.target));
        }
        point1 = new Point(x + 1, y);
        if (!checked.containsKey(point1) && tileIsEmpty(point1)) {
            unChecked.add(new DistanceToPoint(point1, poll.distance + 1, poll.target));
        }
        point1 = new Point(x, y + 1);
        if (!checked.containsKey(point1) && tileIsEmpty(point1)) {
            unChecked.add(new DistanceToPoint(point1, poll.distance + 1, poll.target));
        }
    }

}
