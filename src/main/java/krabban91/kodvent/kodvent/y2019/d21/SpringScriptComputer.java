package krabban91.kodvent.kodvent.y2019.d21;

import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class SpringScriptComputer extends IntCodeComputer {
    public SpringScriptComputer(List<Long> program, LinkedBlockingDeque<Long> inputs, LinkedBlockingDeque<Long> outputs) {
        super(program, inputs, outputs);
    }

    public SpringScriptComputer(List<Long> program, LinkedBlockingDeque<Long> inputs) {
        super(program, inputs);
    }

    public SpringScriptComputer(List<Long> program, int noun, int verb) {
        super(program, noun, verb);
    }
}
