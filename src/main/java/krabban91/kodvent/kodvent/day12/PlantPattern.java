package krabban91.kodvent.kodvent.day12;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PlantPattern {
    List<Boolean> pattern = new ArrayList<>();
    boolean producesPlant;

    public PlantPattern(String input) {
        String[] split = input.split(" => ");
        producesPlant = split[1].equals("#");
        split[0].chars().forEach(c-> pattern.add(c==(int)'#'));
    }

    public boolean matches(List<Boolean> pots) {
        return IntStream.range(0,pots.size()).allMatch(i->pots.get(i)==pattern.get(i));
    }

    public boolean getProducesPlant() {
        return producesPlant;
    }
}
