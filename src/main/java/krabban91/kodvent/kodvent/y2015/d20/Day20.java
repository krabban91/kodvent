package krabban91.kodvent.kodvent.y2015.d20;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Day20 {
    int in;

    public Day20() {
        System.out.println("::: Starting Day 20 :::");
        String inputPath = "y2015/d20/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        return getHouseNumber(this::countGifts, this::isFactorOfFirstFourPrimes);
    }

    public long getPart2() {
        return this.getHouseNumber(this::countGiftsLazyElves, this::isFactorOfFirstFourPrimes);
    }

    private long getHouseNumber(Function<Integer, Long> counter, Predicate<Integer> filter) {

        Map<Integer, Long> history = new HashMap<>();
        int target = in;
        int currentHouse = in;
        long currentNumberOfGifts = 0;
        int bestHouse = Integer.MAX_VALUE;
        while (bestHouse != currentHouse) {
            while (currentNumberOfGifts < target) {
                if (filter.test(++currentHouse)) {
                    currentNumberOfGifts = history.computeIfAbsent(currentHouse, counter);
                }
            }
            if (currentHouse != bestHouse) {
                bestHouse = currentHouse;
                currentHouse /= 1.1;
                currentNumberOfGifts = 0;
            }
        }
        return bestHouse;
    }

    private boolean isFactorOfFirstFourPrimes(Integer i) {
        return i % 210 == 0; //2*3*5*7
    }

    public long countGifts(int houseNumber) {
        return IntStream.rangeClosed(1, houseNumber)
                .filter(i -> houseNumber % i == 0)
                .map(i -> i * 10)
                .sum();

    }

    public long countGiftsLazyElves(int houseNumber) {
        return IntStream.rangeClosed(1, houseNumber)
                .filter(i -> houseNumber % i == 0)
                .filter(i -> houseNumber / i <= 50)
                .map(i -> i * 11)
                .sum();

    }

    public void readInput(String inputPath) {
        in = Integer.parseInt(Input.getSingleLine(inputPath));
    }
}
