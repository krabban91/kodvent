package krabban91.kodvent.kodvent.y2015.d06;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class LightGrid {
    private final List<Instruction> instructions;
    private final Map<Point, Boolean> easy;
    private final Map<Point, Integer> hard;

    public LightGrid(List<Instruction> instructions) {
        this.easy = new HashMap<>();
        this.hard = new HashMap<>();
        this.instructions = instructions;
        IntStream.range(0, 1000).forEach(y -> IntStream.range(0, 1000).forEach(x -> {
            easy.put(new Point(x, y), false);
            hard.put(new Point(x, y), 0);
        }));
        this.performInstructions();
    }

    public void performInstructions() {
        instructions.forEach(this::performInstruction);
    }

    public long totalBrightness() {
        return hard.values().stream().reduce(0, Integer::sum);
    }

    private void performInstruction(Instruction instruction) {
        IntStream.rangeClosed(instruction.getFrom().y, instruction.getTo().y)
                .forEach(y -> IntStream.rangeClosed(instruction.getFrom().x, instruction.getTo().x)
                        .forEach(x -> {
                            Point point = new Point(x, y);
                            switch (instruction.getAction()) {
                                case TURN_ON:
                                    turnOnLight(point);
                                    break;
                                case TURN_OFF:
                                    turnOffLight(point);
                                    break;
                                case TOGGLE:
                                    toggleLight(point);
                                    break;
                            }
                        }));
    }

    private void toggleLight(Point point) {
        this.easy.put(point, !this.easy.get(point));

        this.hard.put(point, this.hard.get(point) + 2);
    }

    private void turnOnLight(Point point) {
        this.easy.put(point, true);
        this.hard.put(point, this.hard.get(point) + 1);
    }

    private void turnOffLight(Point point) {
        this.easy.put(point, false);
        this.hard.put(point, Math.max(this.hard.get(point) - 1, 0));
    }

    public long countLitLights() {
        return easy.values().stream().filter(e -> e).count();
    }
}
