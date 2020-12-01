package krabban91.kodvent.kodvent.y2018.d24;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class BattleGroup {

    public static int counter = 1;

    String teamName;
    int number = counter++;
    List<Category> immunities = new ArrayList<>();
    List<Category> weaknesses = new ArrayList<>();
    Category attackType;
    int hitPoints;
    int attackPower;
    int unitsLeft;
    int initiative;

    BattleGroup selectedTarget;
    boolean targeted;
    private boolean debug;

    public BattleGroup(String input, String teamName) {
        String[] split = input.split(" units each with ");
        this.unitsLeft = Integer.parseInt(split[0]);
        String[] split1 = split[1].split(" hit points ");
        this.hitPoints = Integer.parseInt(split1[0]);
        if (split1[1].startsWith("(")) {
            String[] defense = split1[1].split("\\)")[0].split("\\(")[1].split(";");
            IntStream.range(0, defense.length).forEach(i -> {
                if (defense[i].contains("immune")) {
                    String[] immuneTos = defense[i].split("immune to ")[1].split(", ");
                    IntStream.range(0, immuneTos.length).forEach(j -> immunities.add(getCategory(immuneTos[j])));
                } else {
                    String[] immuneTos = defense[i].split("weak to ")[1].split(", ");
                    IntStream.range(0, immuneTos.length).forEach(j -> weaknesses.add(getCategory(immuneTos[j])));
                }
            });
        }
        String[] split2 = split1[1].split("with an attack that does ");
        String[] attack = split2[1].split(" ");
        this.attackPower = Integer.parseInt(attack[0]);
        this.attackType = getCategory(attack[1]);
        String[] split3 = split2[1].split(" at initiative ");
        this.initiative = Integer.parseInt(split3[1]);
        this.teamName = teamName;
    }

    public boolean isTargeted() {
        return targeted;
    }

    public void selectTarget(List<BattleGroup> immuneSystem) {
        Optional<BattleGroup> first = immuneSystem.stream()
                .filter(g -> !g.isTargeted())
                .max(Comparator
                        .comparingInt(g -> ((BattleGroup) g).potentialDamage(this.effectivePower(), attackType))
                        .thenComparing(g -> ((BattleGroup) g).effectivePower())
                        .thenComparing(g -> ((BattleGroup) g).getInitiative()));
        if (first.isPresent()) {
            if(first.get().potentialDamage(this.effectivePower(),attackType) >0){
                this.selectedTarget = first.get();
                this.selectedTarget.targeted = true;
            }

            if (debug) {
                reportTargetSelection(first);
            }
        }
    }

    private void reportTargetSelection(Optional<BattleGroup> first) {
        System.out.println(String.format("%s group %s would deal defending group %s %s damage",
                teamName, number, first.get().number, first.get().potentialDamage(effectivePower(), attackType)));
    }

    public void startRound(){
        this.targeted = false;
        this.selectedTarget = null;
    }

    public int getInitiative() {
        return initiative;
    }

    public int effectivePower() {
        return unitsLeft * attackPower;
    }

    public int potentialDamage(int effectivePower, Category category) {
        if (immunities.contains(category)) {
            return 0;
        }
        if (weaknesses.contains(category)) {
            return 2 * effectivePower;
        }
        return effectivePower;
    }

    public boolean hasTarget() {
        return this.selectedTarget != null;
    }

    public void attackTarget() {
        if(isAlive()){
            int unitsLeft = this.selectedTarget.unitsLeft;
            this.selectedTarget.takeDamage(effectivePower(), attackType);

            if (debug) {
                reportAttack(unitsLeft);
            }
        }
    }

    private void reportAttack(int unitsLeft) {
        System.out.println(String.format("%s group %s attacks defending group %s, killing %s units.", teamName, number, this.selectedTarget.number, unitsLeft - Math.max(0, this.selectedTarget.unitsLeft)));
    }

    public  void reportHealth() {
        System.out.println(String.format("Group %s contains %s units", number, unitsLeft));
    }

    public void boost(int boost) {
        this.attackPower+=boost;
    }

    public void activateDebugging() {
        this.debug = true;
    }

    private void takeDamage(int attackPower, Category attackType) {
        int damage = potentialDamage(attackPower, attackType);
        int unitsLost = damage/this.hitPoints;
        this.unitsLeft -=unitsLost;
    }

    public boolean isAlive() {
        return unitsLeft >0;
    }

    public int getUnitsLeft() {
        return unitsLeft;
    }

    private Category getCategory(String s) {
        switch (s) {
            case "fire":
                return Category.FIRE;
            case "cold":
                return Category.COLD;
            case "slashing":
                return Category.SLASHING;
            case "bludgeoning":
                return Category.BLUDGEONING;
            case "radiation":
                return Category.RADIATION;
            default:
                return Category.FIRE;
        }
    }
}
