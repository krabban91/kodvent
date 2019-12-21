package krabban91.kodvent.kodvent.y2019.d21;

import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class SpringScriptComputer extends IntCodeComputer {
    private List<String> instructions = new ArrayList<>();

    public SpringScriptComputer(List<Long> program, LinkedBlockingDeque<Long> inputs, LinkedBlockingDeque<Long> outputs) {
        super(program, inputs, outputs);
    }

    public void addSpringScriptInstructions(String instruction) {
        instructions.add(instruction);
    }

    public void activateInstructions() {
        instructions.forEach(instr -> {
                    instr.chars().boxed().collect(Collectors.toList()).forEach(i -> this.addInput(i.longValue()));
                    this.addInput('\n');
                }
        );
    }
}
