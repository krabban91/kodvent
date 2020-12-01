package krabban91.kodvent.kodvent.y2018.d24;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Battle {

    private List<BattleGroup> immuneSystem;
    private List<BattleGroup> infection;
    private List<String> input;
    private boolean debug;

    public Battle(List<String> input) {
        this.input = input;
        resetTeams(0);
    }

    public void activateDebugging() {
        this.debug = true;
        this.immuneSystem.forEach(BattleGroup::activateDebugging);
        this.infection.forEach(BattleGroup::activateDebugging);
    }

    public void resetTeams(int boost) {
        debug = false;
        BattleGroup.counter = 1;
        List<String> immuneSystemStrings = input.subList(input.indexOf("Immune System:") + 1, input.indexOf(""));
        immuneSystem = immuneSystemStrings.stream().map(s -> new BattleGroup(s,"Immune System")).collect(Collectors.toList());
        immuneSystem.forEach(g -> g.boost(boost));
        BattleGroup.counter = 1;
        List<String> infectionsStrings = input.subList(input.indexOf("Infection:") + 1, input.size());
        infection = infectionsStrings.stream().map(s -> new BattleGroup(s,"Infection")).collect(Collectors.toList());
    }

    public long unitsLeftInWinningArmy() {
        while (immuneSystem.size() > 0 && infection.size() > 0) {
            int unitsBefore = immuneSystem.stream().map(BattleGroup::getUnitsLeft).reduce(0, Integer::sum) +
                    infection.stream().map(BattleGroup::getUnitsLeft).reduce(0, Integer::sum);
            startBattle();

            //targeting phase
            targetPhase();
            //attacking phase
            attackPhase();

            cleanUpBattle();
            int unitsAfter = immuneSystem.stream().map(BattleGroup::getUnitsLeft).reduce(0, Integer::sum) +
                    infection.stream().map(BattleGroup::getUnitsLeft).reduce(0, Integer::sum);
            if(unitsAfter==unitsBefore){
                return 0;
            }
        }
        return immuneSystem.size() > 0 ?
                immuneSystem.stream().map(BattleGroup::getUnitsLeft).reduce(0, Integer::sum) :
                infection.stream().map(BattleGroup::getUnitsLeft).reduce(0, Integer::sum);
    }

    public long unitsLeftWhenGivingTheSmallestPossibleBoost(){
        int boostIncrement = 1000000;
        int boost = 0;
        resetTeams(boost);
        while (!infection.isEmpty()){
            System.out.println("boost is: "+boost);
            long l = unitsLeftInWinningArmy();
            if(l == 0 || immuneSystem.isEmpty()){
                boost+=boostIncrement;
                resetTeams(boost);
            } else{
                if(boostIncrement == 1){
                    return l;
                }
                boost -= boostIncrement;
                boostIncrement/=10;
                boost += boostIncrement;
                resetTeams(boost);
            }
        }
        return 0;

    }

    private void startBattle() {
        this.infection.stream().forEach(BattleGroup::startRound);
        this.immuneSystem.stream().forEach(BattleGroup::startRound);
        if(debug){
            System.out.println("Immune System:");
            this.immuneSystem.forEach(BattleGroup::reportHealth);
            System.out.println("Infection:");
            this.infection.forEach(BattleGroup::reportHealth);
            System.out.println();
        }


    }

    private void cleanUpBattle() {
        this.infection = infection.stream().filter(BattleGroup::isAlive).collect(Collectors.toList());
        this.immuneSystem = immuneSystem.stream().filter(BattleGroup::isAlive).collect(Collectors.toList());
    }

    private void targetPhase() {
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

        if(debug){
            System.out.println();
        }
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

        if(debug){
            System.out.println();
        }
    }
}
