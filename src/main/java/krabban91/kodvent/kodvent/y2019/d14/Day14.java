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
        final int million = 1000000;
        final long oneTrillion = 1000000000000L;
        long iterationSize = oneTrillion / million;
        long iterator = 0;
        boolean b = false;
        while (b) {
            long l = bottomUp(1) * iterator;
            if (l < oneTrillion) {
                if (iterationSize == 1) {
                    return iterator;
                } else {
                    iterator += iterationSize;
                }
            } else {
                iterator -= iterationSize;
                iterationSize /= 2;
            }
        }
        return givenOre();
    }


    private long givenOre() {

        final long oneTrillion = 1000000000000L;
        long factor = (long) Math.pow(2,28);
        HashMap<String, Long> copy = null;
        Long costOfOneFuel = null;
        Long sizeCopy = null;
        final Map<String, Reaction> reactions = in.stream().collect(Collectors.toMap(e -> e.output, e -> e));

        final HashMap<String, Long> currently = new HashMap<>(); // name, count
        AtomicLong fuelCreated = new AtomicLong();
        AtomicLong usedOre = new AtomicLong();
        while (usedOre.get() < oneTrillion) {
            while (usedOre.get() < oneTrillion && !(currently.containsKey("FUEL"))) {

                Reaction pointer = reactions.get("FUEL");
                while (true) {
                    if (pointer.hasEnough(currently)) {
                        break;
                    } else {
                        final Optional<String> nextInput = pointer.getNextInput(currently);
                        if (nextInput.isEmpty()) {
                            // should not happen happen in part 2
                            break;
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
            // calculate ores used
            if (costOfOneFuel == null) {
                costOfOneFuel = usedOre.get();
                copy = new HashMap<>(currently);
                sizeCopy = copy.values().stream().mapToLong(e->e).sum();
            }
            if (false && currently.values().stream().mapToLong(e->e).sum() <sizeCopy * 2) {
                final long increment = costOfOneFuel * factor;
                boolean enteredLoop = false;
                if (usedOre.get() + increment < (oneTrillion - increment * 2 * 2)) {
                    enteredLoop = true;
                    final long finalFactor = factor;
                    final HashMap<String, Long> finalCopy = copy;
                    finalCopy.forEach((k, v) -> {
                        currently.computeIfPresent(k, (kk, vv) -> vv + finalFactor * finalCopy.get(kk));
                        currently.putIfAbsent(k, finalCopy.get(k));
                    });
                    usedOre.addAndGet(increment);
                }
                if(!enteredLoop){
                    factor = Math.max(1,factor/2);

                }
            }
            if(currently.containsKey("FUEL")){
                fuelCreated.addAndGet(currently.remove("FUEL"));
            }
        }

        return fuelCreated.get();

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
