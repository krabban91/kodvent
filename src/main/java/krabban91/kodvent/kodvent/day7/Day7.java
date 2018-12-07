package krabban91.kodvent.kodvent.day7;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Day7 {
    private static String inputPath = "day7.txt";
    List<Order> orderList;
    PriorityQueue<Character> avaliableMoves = new PriorityQueue<>(Character::compareTo);
    Map<Character, List<Character>> blockingMoves = new HashMap<>();
    StringBuilder performed = new StringBuilder();

    public String getPart1() {
        while (!avaliableMoves.isEmpty()) {
            Character poll = avaliableMoves.poll();
            List<Character> remove = blockingMoves.remove(poll);
            if (remove != null) {
                remove.stream()
                        .filter(order -> !blockingMoves.values()
                                .stream()
                                .anyMatch(l -> l.contains(order)))
                        .forEach(order -> {
                            if (!avaliableMoves.contains(order)) {
                                avaliableMoves.add(order);
                            }
                        });
            }
            performed.append(poll);

        }
        return performed.toString();
    }


    public int getPart2() {
        return -1;
    }

    public Order parseRow(String row) {
        return new Order(row);
    }

    public List<Order> readInput(Stream<String> stream) {
        return stream.map(this::parseRow).collect(Collectors.toList());
    }

    private void addToAvailable(Order order){
        if (!avaliableMoves.contains(order.getOther())) {
            avaliableMoves.add(order.getOther());
        }
    }

    private void addToBlocking(Order order) {

        if (avaliableMoves.contains(order.getSelf())) {
            avaliableMoves.remove(order.getSelf());
        }
        if (!blockingMoves.containsKey(order.getOther())) {
            blockingMoves.put(order.getOther(), new LinkedList<>());
        }
        List<Character> characters = blockingMoves.get(order.getOther());
        if (!characters.contains(order.getSelf())) {
            characters.add(order.getSelf());
        }
    }

    public Day7() {
        System.out.println("::: Starting Day 7 :::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            orderList = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        orderList.forEach(this::addToAvailable);
        orderList.forEach(this::addToBlocking);
        String part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
