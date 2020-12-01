package krabban91.kodvent.kodvent.y2015.d12;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import krabban91.kodvent.kodvent.utilities.Input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@Component
public class Day12 {
    String in;

    public long getPart1() {
        return sumOfNumbers(in, false);
    }

    public long getPart2() {
        return sumOfNumbers(in, true);
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).get(0);
    }

    public List<Integer> getAllNumbers(String input, boolean ignoreRed) {
        Object map = new Gson().fromJson(input, Object.class);
        return getNumbers(map, ignoreRed);
    }

    public List<Integer> getNumbers(Object input, boolean ignoreRed) {
        if (input instanceof ArrayList) {
            ArrayList list = (ArrayList) input;
            ArrayList<Integer> objects = new ArrayList<>();
            list.forEach(e -> objects.addAll(getNumbers(e, ignoreRed)));
            return objects;
        }
        if (input instanceof Double) {
            return Collections.singletonList((((Double) input).intValue()));
        }
        if (input instanceof LinkedTreeMap) {
            LinkedTreeMap tree = (LinkedTreeMap) input;
            if (ignoreRed && containsRed(tree)) {
                return Collections.emptyList();
            }
            ArrayList<Integer> objects = new ArrayList<>();
            tree.values().forEach(e -> objects.addAll(getNumbers(e, ignoreRed)));
            return objects;
        }
        return Collections.emptyList();
    }

    private boolean containsRed(Object input) {
        if (input instanceof ArrayList) {
            return false;
        }
        if (input instanceof LinkedTreeMap) {
            LinkedTreeMap tree = (LinkedTreeMap) input;
            return tree.values().stream().anyMatch(v -> v.equals("red"));
        }
        return input.equals("red");
    }

    public int sumOfNumbers(String input, boolean ignoreRed) {
        return getAllNumbers(input, ignoreRed)
                .stream()
                .mapToInt(e -> e)
                .sum();
    }

    public Day12() {
        System.out.println("::: Starting Day 12 :::");
        String inputPath = "y2015/d12/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
