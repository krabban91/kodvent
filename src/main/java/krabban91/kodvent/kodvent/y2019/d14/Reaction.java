package krabban91.kodvent.kodvent.y2019.d14;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reaction {

    Map<String, Integer> input;
    String output;
    int quantity;

    public Reaction(String s) {

        final String[] split = s.split("=>");
        input = Stream.of(split[0].trim().split(",")).map(e -> e.trim().split(" ")).collect(Collectors.toMap(e -> e[1], e -> Integer.parseInt(e[0])));
        final String[] out = split[1].trim().split(" ");
        output = out[1];
        quantity = Integer.parseInt(out[0]);
    }

    public boolean hasEnough(HashMap<String, Long> currently) {
        return input.entrySet().stream()
                .allMatch(e -> (currently.containsKey(e.getKey()) &&
                        currently.get(e.getKey()) >= e.getValue())) ||
                input.keySet().size() == 1 && input.keySet().contains("ORE");
    }

    public Optional<String> getNextInput(HashMap<String, Long> currently) {
        return input.entrySet().stream()
                .filter(e -> (!currently.containsKey(e.getKey())) ||
                        (currently.containsKey(e.getKey()) &&
                                currently.get(e.getKey()) < e.getValue()))
                .map(e -> e.getKey())
                .findFirst();
    }
}
