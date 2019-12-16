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
        // return bottomUp(1);
        return topDown(1);
    }


    public long getPart2() {
        final long oneTrillion = 1000000000000L;
        long iterationSize = oneTrillion;
        long iterator = 0;
        long best = 0;
        while (true) {
            long l = topDown(iterator);
            if (l <= oneTrillion) {
                best = iterator;
                iterator += iterationSize;
            } else {
                if (iterationSize == 1) {
                    return best;
                }
                iterator -= iterationSize;
                iterationSize /= 2;
            }
        }
    }

    // much faster version
    private long topDown(long expected) {
        final Map<String, Reaction> reactions = in.stream().collect(Collectors.toMap(e -> e.output, e -> e));
        final HashMap<String, Long> toTrade = new HashMap<>();
        final HashMap<String, Long> leftOvers = new HashMap<>();
        if (expected <= 0) {
            return 0;
        }
        toTrade.put("FUEL", expected);
        while (!(toTrade.size() == 1 && toTrade.containsKey("ORE"))) {
            Optional<Map.Entry<String, Long>> any = toTrade.entrySet().stream()
                    .filter(e -> !e.getKey().equals("ORE"))
                    .findAny();
            if (any.isPresent()) {
                String item = any.get().getKey();
                Long value = any.get().getValue();
                Reaction reaction = reactions.get(item);

                long numberOfReactions = value / reaction.quantity + (value % reaction.quantity == 0 ? 0 : 1);
                long restOutput = numberOfReactions * reaction.quantity - value;
                if (restOutput > 0) {
                    leftOvers.put(item, restOutput);
                }
                reaction.input.forEach((k, v) -> {
                    Long alreadyHad = leftOvers.remove(k);
                    long l = alreadyHad == null ? 0 : alreadyHad;
                    long value1 = numberOfReactions * v.longValue() - l;
                    if (value1 > 0) {
                        toTrade.computeIfPresent(k, (kk, vv) -> vv + value1);
                        toTrade.putIfAbsent(k, value1);
                    } else if (value1 < 0) {
                        leftOvers.put(k, -value1);
                    }
                });
                toTrade.remove(item);
            }
        }
        return toTrade.get("ORE");
    }

    // first version. Retired due to new use cases.
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
                    pointer = reactions.get(nextInput.get());
                }
            }
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
