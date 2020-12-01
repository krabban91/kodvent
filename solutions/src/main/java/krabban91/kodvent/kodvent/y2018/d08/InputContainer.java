package krabban91.kodvent.kodvent.y2018.d08;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InputContainer {
    LinkedList<Integer> input;

    public InputContainer(List<Integer> input) {
        this.input = new LinkedList<>(input.stream().collect(Collectors.toList()));
    }

    public List<Integer> removeNFirst(int end) {
        return IntStream
                .range(0, end)
                .mapToObj(i -> input.removeFirst())
                .collect(Collectors.toList());
    }
}
