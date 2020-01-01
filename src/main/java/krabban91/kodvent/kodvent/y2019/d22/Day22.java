package krabban91.kodvent.kodvent.y2019.d22;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
        List<Long> deck = LongStream.range(0, 10007).boxed().collect(Collectors.toList());
        deck = DealingAction.shuffle(deck, in);
        return deck.indexOf(2019L);
    }


    /**
     * Compose the rules of how shuffling is done into a polynomial ax + b % L.
     *  - done by applying the rules in reverse (to know what is on index x after complete)
     * Update the parameters to correspond to shuffling m times (ax + b )^m % L.
     *  - Parameters updated first using quadratic rules (not gotten a hang of this yet)
     * Apply generic polynomial for initial index ax + b % L 
     *
     *
     * @return
     */
    public long getPart2() {
        BigInteger L = BigInteger.valueOf(119315717514047L);
        BigInteger m = BigInteger.valueOf(101741582076661L);
        BigInteger x = BigInteger.valueOf(2020);
        Map.Entry<BigInteger, BigInteger> parameters = calculatePolynomialParameters(L);
        BigInteger a = parameters.getKey();
        BigInteger b = parameters.getValue();

        Map.Entry<BigInteger, BigInteger> polypow = DealingAction.polypow(a, b, m, L);
        return polypow.getKey().multiply(x).add(polypow.getValue()).mod(L).longValue();
    }


    private Map.Entry<BigInteger, BigInteger> calculatePolynomialParameters(BigInteger size) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = BigInteger.ZERO;
        Map.Entry<BigInteger, BigInteger> pair = Map.entry(a, b);
        ArrayList<DealingAction> dealingActions = new ArrayList<>(in);
        Collections.reverse(dealingActions);
        for (DealingAction action : dealingActions) {
            pair = action.adjustPolynomialParameters(pair, size);
        }
        return pair;
    }


    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).stream().map(DealingAction::new).collect(Collectors.toList());
    }
}
