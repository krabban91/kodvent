package krabban91.kodvent.kodvent.y2019.shared;

import krabban91.kodvent.kodvent.y2019.shared.IntCodeComputer;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class AsciiComputer extends IntCodeComputer {

    public AsciiComputer(List<Long> program) {
        super(program, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
    }

    public void inputAscii(String instruction) {
        (instruction + "\n").chars().boxed().collect(Collectors.toList()).forEach(i -> this.addInput(i.longValue()));

    }
}
