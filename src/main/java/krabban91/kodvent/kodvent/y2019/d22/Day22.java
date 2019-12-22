package krabban91.kodvent.kodvent.y2019.d22;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Component
public class Day22 {
    List<DealingAction> in;

    public Day22() {
        System.out.println("::: Starting Day 22 :::");
        String inputPath = "y2019/d22/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        List<Integer> deck = IntStream.range(0, 10007).boxed().collect(Collectors.toList());
        for (DealingAction action : in) {
            deck = action.handle(deck);
        }
        return deck.indexOf(2019);
    }

    public long getPart2() {
        List<Integer> deck = LongStream.range(0, 119315717514047L).mapToInt(l->(int)l).boxed().collect(Collectors.toList());
        for (long i = 0; i < 101741582076661L; i++) {
            for (DealingAction action : in) {
                deck = action.handle(deck);
            }
        }
        return deck.get(2020);
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).stream().map(DealingAction::new).collect(Collectors.toList());
    }
}
