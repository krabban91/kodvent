package krabban91.kodvent.kodvent.day15;


import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CaveBattle {

    List<List<Boolean>> map;
    List<BattleUnit> units = new ArrayList();
    int round = 0;
    Map<Point, Map<Point, Integer>> distances = new HashMap<>();

    public CaveBattle(List<String> rows) {
        map = rows.stream().map(this::getMapRow).collect(Collectors.toList());
        IntStream.range(0, rows.size()).forEach(y -> IntStream.range(0, rows.get(y).length())
                .filter(x -> rows.get(y).charAt(x) == 'G' || rows.get(y).charAt(x) == 'E')
                .forEach(x -> units.add(new BattleUnit(
                        new Point(x, y),
                        rows.get(y).charAt(x) == 'G',
                        200,
                        this))));
    }

    private void visualizeBattle() {
        System.out.println(": Round " + round + "! :");
        IntStream.range(0, map.size()).forEach(y -> {
            StringBuilder builder = new StringBuilder();
            IntStream.range(0, map.get(y).size()).forEach(x -> {
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
            units.stream()
                    .filter(u ->u.location.y == y)
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
        return units.stream().filter(u -> u.isGoblin() == isGoblin);
    }

    public Stream<BattleUnit> targetsNextToPoint(boolean isGoblin, Point point) {
        return units.stream().filter(u -> u.isGoblin() == isGoblin).filter(unit -> manhattanDistance(unit.getLocation(), point) == 1);
    }

    public static int manhattanDistance(Point point, Point other) {
        return (int) (Math.abs(point.getX() - other.getX()) + Math.abs(point.getY() - other.getY()));
    }


    public boolean battleIsOver() {
        long goblins = units.stream().filter(u -> u.isGoblin()).count();
        return goblins == units.size() || goblins == 0;
    }

    public boolean removeFromBattle(BattleUnit battleUnit) {
        return this.units.remove(battleUnit);
    }

    public boolean canReach(BattleUnit battleUnit, Point point) {
        return distanceBetween(battleUnit.location, point).distance > 0;
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
                        .filter(unit -> unit.getLocation().x == point.x && unit.getLocation().y == point.y)
                        .findAny()
                        .isPresent();
    }

    private Map<Point,Map<Point, DistanceToPoint>> checkedMap = new HashMap<>();


    public DistanceToPoint distanceBetween(Point from, Point to) {
        if(!checkedMap.containsKey(from)){
            checkedMap.put(from, new HashMap<>());
        }
        Map<Point, DistanceToPoint> checked = checkedMap.get(from);
        if (!(checked.containsKey(to))) {
            PriorityQueue<DistanceToPoint> unChecked = new PriorityQueue<>(Comparator.comparingInt(DistanceToPoint::getDistance));
            unChecked.add(new DistanceToPoint(from, 0));
            while (!unChecked.isEmpty()) {
                DistanceToPoint poll = unChecked.poll();
                int x = poll.point.x;
                int y = poll.point.y;
                addPointToChecked(from, poll);
                if (checkedMap.get(poll.point).containsKey(to)){
                    DistanceToPoint fromPollToTo = checkedMap.get(poll.point).get(to);
                    DistanceToPoint fromTo = new DistanceToPoint(to,poll.distance + fromPollToTo.distance);
                    addPointToChecked(from, fromTo);
                }
                Point point1 = new Point(x, y - 1);
                if (!checked.containsKey(point1) && tileIsEmpty(point1)) {
                    unChecked.add(new DistanceToPoint(point1, poll.distance + 1));
                }
                point1 = new Point(x - 1, y);
                if (!checked.containsKey(point1) && tileIsEmpty(point1)) {
                    unChecked.add(new DistanceToPoint(point1, poll.distance + 1));
                }
                point1 = new Point(x + 1, y);
                if (!checked.containsKey(point1) && tileIsEmpty(point1)) {
                    unChecked.add(new DistanceToPoint(point1, poll.distance + 1));
                }
                point1 = new Point(x, y + 1);
                if (!checked.containsKey(point1) && tileIsEmpty(point1)) {
                    unChecked.add(new DistanceToPoint(point1, poll.distance + 1));
                }
            }
            getAllPointsNotIn(checked).forEach(p -> checked.put(p, new DistanceToPoint(p, -1)));
        }
        return checked.get(to);
    }

    private Stream<Point> getAllPointsNotIn(Map<Point, DistanceToPoint> checked) {
        return IntStream.range(0, map.size())
                .mapToObj(y -> IntStream.range(0, map.get(y).size())
                        .mapToObj(x -> new Point(x, y))
                        .filter(p -> !checked.containsKey(p))
                        .collect(Collectors.toList()))
                .flatMap(List::stream);
    }

    private void addPointToChecked(Point from, DistanceToPoint poll) {
        checkedMap.get(from).put(poll.point,poll);
        if(!checkedMap.containsKey(poll.point)){
            checkedMap.put(poll.point, new HashMap<>());
        }
        checkedMap.get(poll.point).put(from, new DistanceToPoint(from, poll.distance));
    }

    public void resetDistanceTable() {
        checkedMap.clear();
    }
}
