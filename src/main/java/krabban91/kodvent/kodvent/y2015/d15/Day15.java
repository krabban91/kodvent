package krabban91.kodvent.kodvent.y2015.d15;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day15 {
    List<Ingredient> in;

    public Day15() {
        System.out.println("::: Starting Day 15 :::");
        String inputPath = "y2015/d15/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        long cake = bruteCake(in, false);
        return cake;
    }

    public long getPart2() {
        return bruteCake(in, true);
    }

    public long score(Map<Ingredient, Integer> ratios) {
        return capacity(ratios) * durability(ratios) * flavor(ratios) * texture(ratios);
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath)
                .stream()
                .map(Ingredient::new)
                .collect(Collectors.toList());
    }

    private long capacity(Map<Ingredient, Integer> ratios) {
        long sum = ratios.entrySet().stream().mapToLong(e -> e.getValue() * e.getKey().getCapacity()).sum();
        return sum < 0 ? 0 : sum;
    }

    private long durability(Map<Ingredient, Integer> ratios) {
        long sum = ratios.entrySet().stream().mapToLong(e -> e.getValue() * e.getKey().getDurability()).sum();
        return sum < 0 ? 0 : sum;
    }

    private long flavor(Map<Ingredient, Integer> ratios) {
        long sum = ratios.entrySet().stream().mapToLong(e -> e.getValue() * e.getKey().getFlavor()).sum();
        return sum < 0 ? 0 : sum;
    }

    private long texture(Map<Ingredient, Integer> ratios) {
        long sum = ratios.entrySet().stream().mapToLong(e -> e.getValue() * e.getKey().getTexture()).sum();
        return sum < 0 ? 0 : sum;
    }

    private long calories(Map<Ingredient, Integer> ratios) {
        long sum = ratios.entrySet().stream().mapToLong(e -> e.getValue() * e.getKey().getCalories()).sum();
        return sum < 0 ? 0 : sum;
    }

    private long bruteCake(List<Ingredient> ingredients, boolean strictCalories) {
        // Not proud of this.
        if (ingredients.size() == 2) {
            return IntStream.rangeClosed(0, 100)
                    .mapToLong(i -> IntStream.rangeClosed(0, 100 - i)
                            .mapToLong(j -> {
                                Map<Ingredient, Integer> ratios = Map.of(
                                        ingredients.get(0), i,
                                        ingredients.get(1), j);
                                return bruteScore(strictCalories, ratios);
                            })
                            .max()
                            .orElse(-1))
                    .max()
                    .orElse(-1);
        }
        if (ingredients.size() == 4) {
            return IntStream.rangeClosed(0, 100)
                    .mapToLong(i -> IntStream.rangeClosed(0, 100 - i)
                            .mapToLong(j -> IntStream.rangeClosed(0, 100 - i - j)
                                    .mapToLong(k -> IntStream.rangeClosed(0, 100 - i - j - k)
                                            .mapToLong(l -> {
                                                Map<Ingredient, Integer> ratios = Map.of(
                                                        ingredients.get(0), i,
                                                        ingredients.get(1), j,
                                                        ingredients.get(2), k,
                                                        ingredients.get(3), l);
                                                return bruteScore(strictCalories, ratios);
                                            }).max().orElse(-1)).max().orElse(-1))
                            .max()
                            .orElse(-1))
                    .max()
                    .orElse(-1);
        }
        return -1;
    }

    private long bruteScore(boolean strictCalories, Map<Ingredient, Integer> ratios) {
        if (strictCalories && this.calories(ratios) != 500) {
            return -1;
        }
        return this.score(ratios);
    }
}
