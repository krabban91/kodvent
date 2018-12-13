package krabban91.kodvent.kodvent.day14;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class RecipeMaker {
    private final String targetMatcher;
    private final int targetRecipeCount;
    private List<Integer> recipes = new ArrayList<>();
    int elf1 = 0;
    int elf2 = 1;

    public RecipeMaker(String input) {
        this.targetMatcher = input;
        this.targetRecipeCount = Integer.parseInt(input);
        addRecipe(3);
        addRecipe(7);
    }

    private void addRecipe(int score) {
        recipes.add(score);
    }

    public int generateUntilScoreMatcherAppears() {
        while (true) {
            takeRecepies();
            addRecipes(recipes.get(elf1) + recipes.get(elf2));
            if (matchFound()) {
                break;
            }
        }
        return indexOfMatch();
    }

    private int indexOfMatch() {
        if (targetMatcher.contains("" + recipes.get(recipes.size() - 1)) || targetMatcher.contains("" + recipes.get(recipes.size() - 2))) {
            StringBuilder builder = new StringBuilder();
            IntStream.range(recipes.size() - targetMatcher.length() - 1, recipes.size()).forEach(i -> builder.append(recipes.get(i)));
            return builder.indexOf(targetMatcher) + recipes.size() - targetMatcher.length() - 1;
        }
        return -1;
    }

    private boolean matchFound() {
        if ((recipes.size() >= targetMatcher.length()) &&
                (targetMatcher.contains("" + recipes.get(recipes.size() - 1)) || targetMatcher.contains("" + recipes.get(recipes.size() - 2)))) {
            StringBuilder builder = new StringBuilder();
            IntStream.range(recipes.size() - targetMatcher.length() - 1, recipes.size()).forEach(i -> builder.append(recipes.get(i)));
            return builder.indexOf(targetMatcher) != -1;
        }
        return false;
    }

    public String generateRecipes() {
        reportStatus();
        while (recipes.size() < targetRecipeCount + 10) {
            takeRecepies();
            addRecipes(recipes.get(elf1) + recipes.get(elf2));
            //reportStatus();
        }
        StringBuilder builder = new StringBuilder();
        IntStream.range(targetRecipeCount, targetRecipeCount + 10).forEach(i -> builder.append(recipes.get(i)));
        return builder.toString();
    }

    private void takeRecepies() {
        elf1 = (elf1 + recipes.get(elf1) + 1) % recipes.size();
        elf2 = (elf2 + recipes.get(elf2) + 1) % recipes.size();
    }

    private void reportStatus() {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, recipes.size()).forEach(i -> {
            if (i == elf1) {
                builder.append('(');
                builder.append(recipes.get(i));
                builder.append(')');
            } else if (i == elf2) {
                builder.append('[');
                builder.append(recipes.get(i));
                builder.append(']');
            } else {
                builder.append(recipes.get(i) + " ");
            }
        });
        System.out.println(builder.toString());

    }

    private void addRecipes(int sum) {
        if (sum > 9) {
            int single = sum % 10;
            int ten = sum / 10;
            addRecipe(ten);
            addRecipe(single);
        } else {
            addRecipe(sum);
        }
    }

}
