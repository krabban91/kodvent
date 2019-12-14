package krabban91.kodvent.kodvent.y2019.d14;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class Day14 {
    List<Reaction> in;

    public Day14() {
        System.out.println("::: Starting Day 14 :::");
        String inputPath = "y2019/d14/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        return bottomUp(1);
    }


    public long getPart2() {
        return -1;
    }

    private long bottomUp(long expected) {
        final Map<String, Reaction> reactions = in.stream().collect(Collectors.toMap(e -> e.output, e -> e));
        final HashMap<String, Long> currently = new HashMap<>(); // name, count
        AtomicLong usedOre = new AtomicLong();
        while (!currently.containsKey("FUEL") || currently.get("FUEL") < expected) {
            Reaction pointer = reactions.get("FUEL");
            while (true) {
                if (pointer.hasEnough(currently)) {
                    break;
                } else {
                    final Optional<String> nextInput = pointer.getNextInput(currently);
                    if (nextInput.isEmpty()) {
                        // Should Never be here.
                        return -1;
                    }
                    pointer = reactions.get(nextInput.get());
                }
            }
            // react!
            if (pointer.input.containsKey("ORE")) {
                usedOre.addAndGet(pointer.input.get("ORE"));
            } else {
                pointer.input.forEach((k, v) -> {
                    final Long count = currently.get(k);
                    final long left = count - v;
                    if (left == 0) {
                        currently.remove(k);
                    } else {
                        currently.put(k, left);
                    }
                });
            }
            final long quantity = pointer.quantity;
            currently.computeIfPresent(pointer.output, (k, v) -> v + quantity);
            currently.putIfAbsent(pointer.output, quantity);


        }
        return usedOre.get();
    }

    public void readInput(String inputPath) {

        in = Input.getLines(inputPath).stream()
                .map(Reaction::new)
                .collect(Collectors.toList());
    }
}
