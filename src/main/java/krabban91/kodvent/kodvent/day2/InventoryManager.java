package krabban91.kodvent.kodvent.day2;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InventoryManager {

    private static String inputPath = "day2.txt";
    // keeps track of number of each letter in each ID
    private List<Map<Integer, Integer>> inventory = new LinkedList<>();
    private List<List<Integer>> words = new LinkedList<>();

    public InventoryManager() {
        System.out.println("::: Starting Day 2:::");
        long part1 = 0;
        part1 = getPart1();
        System.out.println("::: answer to part 1:::");
        System.out.println(part1);
        String part2 = getPart2();
        System.out.println("::: answer to part 2:::");
        System.out.println(part2);

    }

    private String getPart2() {
        return words.stream().filter(this::matchesIdWithOneOther).findAny().get().stream().collect(StringBuilder::new,
                StringBuilder::appendCodePoint, StringBuilder::append).toString();

    }


    private boolean matchesIdWithOneOther(List<Integer> curr) {
        return words
                .stream()
                .filter(m -> m.size() == curr.size())
                .filter(e -> this.almostMatch(e, curr)).count() == 1;
    }

    private boolean almostMatch(List<Integer> item, List<Integer> other) {
        boolean differByOne = false;
        for(int i = 0; i < item.size(); i++){
            if(item.get(i) != other.get(i)){
                if(differByOne){
                    return false;
                }
                differByOne = true;
            }
        }
        return differByOne;
        }


    private long getPart1() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            stream.forEach(this::inventoryRoundup);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.inventoryCheck();
    }

    private long inventoryCheck() {
        int doubles= inventory.stream().mapToInt(m -> m.values().contains(2)? 1: 0).sum();
        int triples= inventory.stream().mapToInt(m -> m.values().contains(3)? 1: 0).sum();
        System.out.println("doubles:"+doubles+ ", triples: "+triples);
        return doubles*triples;

    }


    private String inventoryRoundup(String s) {
        Map<Integer,Integer> values = new HashMap<>();
        List<Integer> string = new LinkedList<>();
        s.chars().forEach(letter -> {
            string.add(letter);
            if(values.containsKey(letter)){
                values.put(letter, values.get(letter)+1);
            }else {
                values.put(letter, 1);
            }
        });
        this.words.add(string);
        this.inventory.add(values);
        return s;
    }

}
