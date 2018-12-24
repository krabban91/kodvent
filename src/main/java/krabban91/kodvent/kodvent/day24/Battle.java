package krabban91.kodvent.kodvent.day24;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Battle {

    List<BattleGroup> immuneSystem;
    List<BattleGroup> infection;

    public Battle(List<String> input) {
        List<String> immuneSystemStrings = input.subList(input.indexOf("Immune System:") + 1, input.indexOf(""));
        immuneSystem = immuneSystemStrings.stream().map(s -> new BattleGroup(s,"Immune System")).collect(Collectors.toList());
        BattleGroup.counter = 1;
        List<String> infectionsStrings = input.subList(input.indexOf("Infection:") + 1, input.size());
        infection = infectionsStrings.stream().map(s -> new BattleGroup(s,"Infection")).collect(Collectors.toList());
    }

    public long unitsLeftInWinningArmy() {
        while (immuneSystem.size() > 0 && infection.size() > 0) {
            startBattle();
            //targeting phase
            targetPhase();
            //attacking phase
            attackPhase();

            cleanUpBattle();
        }
        return immuneSystem.size() > 0 ?
                immuneSystem.stream().map(BattleGroup::getUnitsLeft).reduce(0, Integer::sum) :
                infection.stream().map(BattleGroup::getUnitsLeft).reduce(0, Integer::sum);
    }

    private void startBattle() {
        this.infection.stream().forEach(BattleGroup::startRound);
        this.immuneSystem.stream().forEach(BattleGroup::startRound);

        System.out.println("Immune System:");
        this.immuneSystem.forEach(BattleGroup::reportHealth);
        System.out.println("Infection:");
        this.infection.forEach(BattleGroup::reportHealth);
        System.out.println(" ");


    }

    private void cleanUpBattle() {
        this.infection = infection.stream().filter(BattleGroup::isAlive).collect(Collectors.toList());
        this.immuneSystem = immuneSystem.stream().filter(BattleGroup::isAlive).collect(Collectors.toList());
    }

    private void targetPhase() {
/*
            Not doing it by the book.
* each group attempts to choose one target. In decreasing order of effective power, groups choose their targets; in a tie, the group with the higher initiative chooses first.
* */

        immuneSystem.stream()
                .sorted(Comparator
                        .comparingInt(g -> ((BattleGroup) g).effectivePower())
                        .thenComparingInt(g -> ((BattleGroup) g).getInitiative())
                        .reversed())
                .forEach(group -> group.selectTarget(infection));
        infection.stream()
                .sorted(Comparator
                        .comparingInt(g -> ((BattleGroup) g).effectivePower())
                        .thenComparingInt(g -> ((BattleGroup) g).getInitiative())
                        .reversed())
                .forEach(group -> group.selectTarget(immuneSystem));
        System.out.println();
    }

    private void attackPhase() {
        List<BattleGroup> allGroups = new ArrayList<>();
        allGroups.addAll(immuneSystem);
        allGroups.addAll(infection);
        allGroups.stream()
                .filter(BattleGroup::hasTarget)
                .sorted(Comparator
                        .comparingInt(BattleGroup::getInitiative)
                        .reversed())
                .forEach(BattleGroup::attackTarget);
        System.out.println();
    }
}
