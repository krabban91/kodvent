package krabban91.kodvent.kodvent.day15;

import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

;

public class BattleUnit {

    boolean goblin;
    int hitpoints;
    int attackPower;
    Point location;
    CaveBattle battle;

    public BattleUnit(Point location, boolean goblin, int hitpoints, CaveBattle battle) {
        this.location = location;
        this.goblin = goblin;
        this.hitpoints = hitpoints;
        this.attackPower = 3;
        this.battle = battle;
    }

    private void move() {
        if(battle.battleIsOver()){
            return;
        }
        if (battle.targetsNextToPoint(!goblin, this.getLocation()).count() > 0) {
            return;
        }
        if(battle.targets(!goblin).count() == 0){
            battle.endBattle();
            return;
        }
        Stream<BattleUnit> targets = battle.targets(!goblin);

        Stream<Point> inRange = targets
                .map(battle::avaliableStrikingLocations)
                .flatMap(List::stream);
        Stream<Point> reachable = inRange
                .filter(point -> battle.canReach(this, point));
        Stream<DistanceToPoint> nearest = reachable.map(e -> battle.distanceBetween(this.location, e))
                .filter(p -> p != null && p.distance > 0 && p.distance < CaveBattle.UNREACHABLE_DISTANCE)
                .sorted(DistanceToPoint::compare);
        Optional<DistanceToPoint> chosen = nearest.findFirst();
        if (chosen.isPresent()) {
            List<Point> destinations = new LinkedList<>();
            int x = this.location.x;
            int y = this.location.y;
            destinations.add(new Point(x, y - 1));
            destinations.add(new Point(x - 1, y));
            destinations.add(new Point(x + 1, y));
            destinations.add(new Point(x, y + 1));
            Optional<DistanceToPoint> moveLocation = battle.avaliableStrikingLocations(this)
                    .stream()
                    .map(e -> battle.distanceBetween(chosen.get().point, e))
                    .filter(p -> p != null && p.distance < CaveBattle.UNREACHABLE_DISTANCE)
                    .sorted(DistanceToPoint::compare)
                    .findFirst();
            if (moveLocation.isPresent()) {
                battle.resetDistanceTable();
                reportMovement(isGoblin(), this.location, moveLocation.get().point);
                this.location = moveLocation.get().point;

            } else {
                System.out.println("THIS SHOULDN'T HAPPEN!");
                reportHalting(this);

            }
        } else {
            reportHalting(this);
        }
    }


    private void attack() {
        Stream<BattleUnit> targets = battle.targetsNextToPoint(!goblin, this.getLocation());
        Optional<BattleUnit> first = targets
                .sorted(this::attackCompare)
                .findFirst();
        if (first.isPresent()) {
            this.dealDamage(first.get());
        }

    }

    public int attackCompare(BattleUnit l, BattleUnit r) {
        int compare = Integer.compare(l.getHitpoints(), r.getHitpoints());
        if (compare == 0) {
            return battle.order(l, r);
        }
        return compare;
    }

    private void dealDamage(BattleUnit unit) {
        unit.takeDamage(this.attackPower);
        reportBeating(this, unit, attackPower);
    }

    private static void reportHalting(BattleUnit unit) {
        StringBuilder builder = new StringBuilder();

        if (unit.isGoblin()) {
            builder.append("G");
        } else {
            builder.append("E");
        }
        builder.append("[");
        builder.append(unit.location.x);
        builder.append(",");
        builder.append(unit.location.y);
        builder.append("]");
        builder.append(" halted.");

        System.out.println(builder.toString());


    }


    private static void reportMovement(boolean isGoblin, Point oldLocation, Point newLocation) {
        StringBuilder builder = new StringBuilder();
        if (isGoblin) {
            builder.append("G");
        } else {
            builder.append("E");
        }
        builder.append(" moves from ");
        builder.append(oldLocation.x + "," + oldLocation.y);
        builder.append(" to ");
        builder.append(newLocation.x + "," + newLocation.y);
        System.out.println(builder.toString());
    }

    private void reportBeating(BattleUnit battleUnit, BattleUnit unit, int damage) {
        StringBuilder builder = new StringBuilder();
        if (battleUnit.isGoblin()) {
            builder.append("G");
        } else {
            builder.append("E");
        }

        builder.append("[");
        builder.append(battleUnit.location.x);
        builder.append(",");
        builder.append(battleUnit.location.y);
        builder.append("]");
        builder.append(" strikes ");
        if (unit.isGoblin()) {
            builder.append("G");
        } else {
            builder.append("E");
        }
        builder.append("[");
        builder.append(unit.location.x);
        builder.append(",");
        builder.append(unit.location.y);
        builder.append("]");
        builder.append(" ");
        builder.append(unit.hitpoints);
        builder.append("(was " + (unit.hitpoints + damage) + ").");
        System.out.println(builder.toString());

    }

    private void takeDamage(int hitpoints) {
        this.hitpoints -= hitpoints;
        if (!isAlive()) {
            battle.removeFromBattle(this);
        }
    }

    public boolean isGoblin() {
        return goblin;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public boolean isAlive() {
        return hitpoints > 0;
    }

    public Point getLocation() {
        return location;
    }

    public CaveBattle getBattle() {
        return battle;
    }

    public static void movaAndAttack(BattleUnit battleUnit) {
        if (battleUnit.isAlive()) {
            battleUnit.move();
            battleUnit.attack();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.isGoblin()) {
            builder.append("G");
        } else {
            builder.append("E");
        }
        builder.append("(");
        builder.append(this.hitpoints);
        builder.append("), ");
        return builder.toString();
    }
}
