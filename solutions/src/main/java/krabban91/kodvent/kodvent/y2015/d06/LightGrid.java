package krabban91.kodvent.kodvent.y2015.d06;

import krabban91.kodvent.kodvent.utilities.Grid;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LightGrid {
    private final List<Instruction> instructions;
    private final Grid<Light> grid;

    public LightGrid(List<Instruction> instructions) {
        this.instructions = instructions;
        this.grid = new Grid<>(IntStream.range(0, 1000).mapToObj(y -> IntStream.range(0, 1000).mapToObj(x -> new Light()).collect(Collectors.toList())).collect(Collectors.toList()));
        this.instructions.forEach(this::performInstruction);
    }

    private void performInstruction(Instruction instruction) {
        grid.forEachRangeClosed(instruction.getFrom().x, instruction.getTo().x, instruction.getFrom().y, instruction.getTo().y,
                (x, y) -> grid.get(x, y).get().actOnInstruction(instruction));
    }

    public long totalBrightness() {
        return grid.sum(Light::getBrightness);
    }


    public long countLitLights() {
        return grid.sum(Light::isOn);
    }
}
