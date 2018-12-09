package krabban91.kodvent.kodvent.day9;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Marble {

    private MarbleRules rules;

    private Map<Integer, Long> elfScores;
    private CircularList<MarbleBall> marbles;
    private int currentElfInc;
    private int lastPlayedMarble;
    private int lastPlayedMarbleIndex;
    private boolean debug;


    public Marble(MarbleRules rules) {
        this.rules = rules;
    }

    public void playMarble() {
        this.resetGame();
        if (debug) {
            System.out.println(String.format("[-] %s", marbles.toString()));
        }
        while (lastPlayedMarble < rules.getHighestMarbleValue()) {
            makeMove();
        }
    }

    public long getWinningScore() {
        return getWinner()
                .getValue();
    }

    public static int nextMarbleIndex(int lastPlayedMarbleIndex, int offset, int size) {
        if (size == 1) {
            return 1;
        }
        int index = (lastPlayedMarbleIndex + offset);
        if (index < 0) {
            return (index % size) + size;
        }
        if (index <= size) {
            return index;
        }
        return index % size;
    }

    private void makeMove() {
        int elf = getAndIncCurrentElf();
        int marbleValue = ++lastPlayedMarble;
        if (marbleValue % 100000 == 0) System.out.println(marbleValue);
        if (marbleValue % 23 == 0) {
            elfScores(elf, marbleValue);
        } else {
            lastPlayedMarbleIndex = nextMarbleIndex(lastPlayedMarbleIndex, 2, marbles.size());
            marbles.add(lastPlayedMarbleIndex, new MarbleBall(marbleValue));
        }
        if (debug) {
            System.out.println(String.format("[%s] %s", elf, marbles.toString()));
        }
    }

    private void elfScores(int elf, int marbleValue) {
        int scoreAddition = cashInPoints(marbleValue);
        long totalScore = elfScores.get(elf) + scoreAddition;
        elfScores.put(elf, totalScore);
    }

    private int cashInPoints(int marbleValue) {
        lastPlayedMarbleIndex = nextMarbleIndex(lastPlayedMarbleIndex, -7, marbles.size());
        MarbleBall marbleToRemove = marbles.remove(lastPlayedMarbleIndex);
        int bonus = marbleToRemove.getValue();
        return marbleValue + bonus;
    }

    public int getAndIncCurrentElf() {
        return (currentElfInc++ % rules.getNumberOfPlayers()) + 1;
    }

    private Map.Entry<Integer, Long> getWinner() {
        return elfScores.entrySet().stream().max(Comparator.comparingLong(e -> e.getValue())).get();
    }

    public void setDebug(boolean mode) {
        debug = mode;
    }

    private void resetGame() {
        elfScores = new HashMap<>();
        IntStream.range(1, rules.numberOfPlayers + 1).forEach(this::addElf);
        marbles = new CircularList<>();
        currentElfInc = 0;
        lastPlayedMarble = 0;
        lastPlayedMarbleIndex = 0;
        marbles.add(0, new MarbleBall(lastPlayedMarble));
    }

    private void addElf(int id) {
        elfScores.put(id, 0L);
    }
}
