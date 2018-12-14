package krabban91.kodvent.kodvent.day14;

import java.util.stream.IntStream;

public class RecipeMaker {
    private final String targetMatcher;
    private final int targetRecipeCount;
    private StringBuilder recipes = new StringBuilder();
    int elf1;
    int elf2;
    private boolean debug;

    public RecipeMaker(String input) {
        this.targetMatcher = input;
        this.targetRecipeCount = Integer.parseInt(input);
        addRecipe(3);
        addRecipe(7);
        elf1 = 0;
        elf2 = 1;
    }

    public String generateRecipes() {
        while (recipes.length() <= targetRecipeCount + 10) {
            if (debug) {
                reportStatus();
            }
            addRecipes();
            takeRecepies();
        }
        if (debug) {
            reportStatus();
        }
        return recipes.substring(targetRecipeCount, targetRecipeCount + 10);
    }

    public int generateUntilScoreMatcherAppears() {
        while (!matchFound()) {
            addRecipes();
            takeRecepies();
        }
        return indexOfMatch();
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private void addRecipes() {
        int sum = getRecipeScore(elf1) + getRecipeScore(elf2);
        if (sum > 9) {
            int single = sum % 10;
            int ten = sum / 10;
            addRecipe(ten);
            addRecipe(single);
        } else {
            addRecipe(sum);
        }
    }

    private void addRecipe(int score) {
        recipes.append(score);
    }

    private void takeRecepies() {
        elf1 = (elf1 + getRecipeScore(elf1) + 1) % recipes.length();
        elf2 = (elf2 + getRecipeScore(elf2) + 1) % recipes.length();
    }

    private int getRecipeScore(int index) {
        return Character.getNumericValue(recipes.charAt(index));
    }

    private boolean matchFound() {
        return indexOfMatch() >= 0;
    }

    private int indexOfMatch() {
        if (shouldInvestigatePatternMatch()) {
            String substring = recipes.substring(recipes.length() - targetMatcher.length() - 1, recipes.length());
            int i = substring.indexOf(targetMatcher);
            if (i < 0) {
                return -1;
            }
            return i + recipes.length() - targetMatcher.length() - 1;
        }
        return -1;
    }

    private boolean shouldInvestigatePatternMatch() {
        return (recipes.length() >= targetMatcher.length()) &&
                (targetMatcher.indexOf(recipes.charAt(recipes.length() - 1)) >= 0 ||
                        targetMatcher.indexOf(recipes.charAt(recipes.length() - 2)) >= 0);
    }

    private void reportStatus() {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, recipes.length()).forEach(i -> {
            if (i == elf1) {
                builder.append('(');
                builder.append(recipes.charAt(i));
                builder.append(')');
            } else if (i == elf2) {
                builder.append('[');
                builder.append(recipes.charAt(i));
                builder.append(']');
            } else {
                builder.append(" " + recipes.charAt(i) + " ");
            }
        });
        System.out.println(builder.toString());
    }
}
