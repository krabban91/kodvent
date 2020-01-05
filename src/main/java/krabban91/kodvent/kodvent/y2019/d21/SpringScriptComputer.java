package krabban91.kodvent.kodvent.y2019.d21;

import krabban91.kodvent.kodvent.y2019.shared.AsciiComputer;
import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class SpringScriptComputer extends AsciiComputer {
    private List<String> instructions = new ArrayList<>();

    public SpringScriptComputer(List<Long> program) {
        super(program);
    }

    public void addSpringScriptInstructions(String instruction) {
        instructions.add(instruction);
    }

    public void activateInstructions() {
        instructions.forEach(this::inputAscii);
    }
}
