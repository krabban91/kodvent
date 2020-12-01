package krabban91.kodvent.kodvent.y2018.d09;

import krabban91.kodvent.kodvent.utilities.Input;

public class Day9 {
    MarbleRules rules;
    Marble game;

    public long getPart1() {

        game.playMarble();
        return game.getWinningScore();
    }

    public long getPart2() {
        rules = new MarbleRules(rules.getNumberOfPlayers(), rules.getHighestMarbleValue() * 100);
        setRules(rules);
        game.playMarble();
        return game.getWinningScore();
    }

    public void readInput(String path) {
        this.rules = new MarbleRules(Input.getSingleLine(path));
    }

    public Day9() {
        System.out.println("::: Starting Day 9 :::");
        String inputPath = "y2018/d09/input.txt";
        readInput(inputPath);
        this.setRules(rules);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public void setRules(MarbleRules rules) {
        this.rules = rules;
        this.game = new Marble(rules);
    }

    public void setDebug(boolean b) {
        game.setDebug(b);
    }
}
