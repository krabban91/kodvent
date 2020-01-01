package krabban91.kodvent.kodvent.y2019.d22;

import krabban91.kodvent.kodvent.utilities.Input;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
        return calculateCard(BigInteger.valueOf(10007), BigInteger.valueOf(1), BigInteger.valueOf(2019), false);
    }

    public long getPart2() {
        return calculateCard(BigInteger.valueOf(119315717514047L), BigInteger.valueOf(101741582076661L), BigInteger.valueOf(2020), true);
    }

    public long oldGetPart1() {
        List<Long> deck = LongStream.range(0, 10007).boxed().collect(Collectors.toList());
        deck = DealingAction.shuffle(deck, in);
        return deck.indexOf(2019L);
    }

    /**
     * Compose the rules of how shuffling is done into a polynomial ax + b % L.
     * - done by applying the rules in reverse (to know what is on index x after complete)
     * Update the parameters to correspond to shuffling m times (ax + b )^m % L.
     * - Parameters updated first using quadratic rules (not gotten a hang of this yet)
     * Apply generic polynomial for initial index ax + b % L
     */
    private long calculateCard(BigInteger l, BigInteger m, BigInteger x, boolean reverse) {
        Map.Entry<BigInteger, BigInteger> parameters = calculatePolynomialParameters(l, reverse);
        BigInteger a = parameters.getKey();
        BigInteger b = parameters.getValue();
        Map.Entry<BigInteger, BigInteger> polypow = DealingAction.polypow(a, b, m, l);
        return polypow.getKey().multiply(x).add(polypow.getValue()).mod(l).longValue();
    }

    private Map.Entry<BigInteger, BigInteger> calculatePolynomialParameters(BigInteger size, boolean reverse) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = BigInteger.ZERO;
        Map.Entry<BigInteger, BigInteger> pair = Map.entry(a, b);
        ArrayList<DealingAction> dealingActions = new ArrayList<>(in);
        if (reverse) {
            Collections.reverse(dealingActions);
        }
        for (DealingAction action : dealingActions) {
            pair = action.adjustInversePolynomialParameters(pair, size, reverse);
        }
        return pair;
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).stream().map(DealingAction::new).collect(Collectors.toList());
    }
}
