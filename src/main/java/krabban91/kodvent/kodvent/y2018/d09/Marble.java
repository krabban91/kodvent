package krabban91.kodvent.kodvent.y2018.d09;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Marble {

    private MarbleRules rules;

    private Map<Integer, Long> elfScores;
    private CircularList<MarbleBall> marbles;
    private AtomicInteger currentElfInc;
    private int lastPlayedMarble;
    private boolean debug;

    public Marble(MarbleRules rules) {
        this.rules = rules;
    }

    public void setDebug(boolean mode) {
        debug = mode;
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

    private void makeMove() {
        int elf = currentElfInc.getAndUpdate(this::nextElfValue);
        int marbleValue = ++lastPlayedMarble;
        if (marbleValue % 23 == 0) {
            marbles.rotate(-7);
            MarbleBall removedMarble = marbles.removeFirst();
            elfScores.put(elf, elfScores.get(elf) + marbleValue + removedMarble.getValue());
        } else {
            marbles.rotate(2);
            marbles.addFirst(new MarbleBall(marbleValue));
        }
        if (debug) {
            System.out.println(String.format("[%s] %s", elf, marbles.toString()));
        }
    }

    private int nextElfValue(int i) {
        return (i % rules.getNumberOfPlayers()) + 1;
    }

    private Map.Entry<Integer, Long> getWinner() {
        return elfScores.entrySet().stream().max(Comparator.comparingLong(e -> e.getValue())).get();
    }


    private void resetGame() {
        elfScores = new HashMap<>();
        IntStream.range(1, rules.numberOfPlayers + 1).forEach(this::addElf);
        marbles = new CircularList<>();
        currentElfInc = new AtomicInteger(1);
        lastPlayedMarble = 0;
        marbles.add(0, new MarbleBall(lastPlayedMarble));
    }

    private void addElf(int id) {
        elfScores.put(id, 0L);
    }
}
