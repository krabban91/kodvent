package krabban91.kodvent.kodvent.y2018.d07;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7 {
    List<Order> orderList;
    PriorityQueue<Character> avaliableMoves = new PriorityQueue<>(Character::compareTo);
    Map<Character, List<Character>> blockingMoves = new HashMap<>();
    StringBuilder performed = new StringBuilder();

    public String getPart1() {
        resetValues();
        while (!avaliableMoves.isEmpty()) {
            Character poll = avaliableMoves.poll();
            completeJob(poll);
        }
        return performed.toString();
    }

    public int getPart2(int numberOfWorkers, int extraTimePerJob) {
        resetValues();
        int timeTaken = 0;
        List<Worker> workers = IntStream.range(0, numberOfWorkers)
                .mapToObj(i -> new Worker())
                .collect(Collectors.toList());
        this.reportStatusHeader(workers.size());
        while (thereIsWorkToBeDone(workers)) {
            work(workers);
            getMoreWork(extraTimePerJob, workers);
            reportStatus(timeTaken, workers);
            timeTaken++;
        }
        return --timeTaken;
    }

    private boolean thereIsWorkToBeDone(List<Worker> workers) {
        return !blockingMoves.isEmpty() || !avaliableMoves.isEmpty() || workers.stream().filter(Worker::isWorking).count()>0;
    }

    private void work(List<Worker> workers) {
        workers.stream()
                .filter(Worker::isWorking)
                .map(Worker::doWork)
                .filter(Objects::nonNull)
                .forEach(this::completeJob);
    }

    private void getMoreWork(int extraTimePerJob, List<Worker> workers) {
        workers.stream()
                .filter(Worker::isAvailable)
                .forEach(w -> this.getAJob(w, extraTimePerJob));
    }

    private void reportStatus(int timeTaken, List<Worker> workers) {
        StringBuilder second = new StringBuilder().append(timeTaken).append('\t').append('\t');
        workers.forEach(i-> second.append(i.getWorkingOn()).append('\t').append('\t').append('\t'));
        second.append(performed.toString());
        System.out.println(second.toString());

    }

    private void reportStatusHeader(int workerCount) {
        StringBuilder second = new StringBuilder().append("Second").append('\t');
        IntStream.range(0,workerCount).forEach(i-> second.append("Worker "+i).append('\t'));
        second.append("Done");
        System.out.println(second.toString());
    }

    private void completeJob(Character poll) {
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

    private void getAJob(Worker worker, int extraCost) {
        if (!avaliableMoves.isEmpty()) {
            Character job = avaliableMoves.poll();
            worker.assignWork(job, getCostForJob(job, extraCost));
        }
    }

    public int getCostForJob(char order, int extraCost) {
        return extraCost + ((int) order - (int) 'A') + 1;
    }

    public List<Order> readInput(Stream<String> stream) {
        return stream.map(this::parseRow).collect(Collectors.toList());
    }

    public Order parseRow(String row) {
        return new Order(row);
    }

    private void resetValues() {
        performed = new StringBuilder();
        orderList.forEach(this::addToAvailable);
        orderList.forEach(this::addToBlocking);
    }

    private void addToAvailable(Order order) {
        if (!avaliableMoves.contains(order.getOther())) {
            avaliableMoves.add(order.getOther());
        }
    }

    private void addToBlocking(Order order) {
        avaliableMoves.remove(order.getSelf());
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
        String inputPath = "y2018/d07/input.txt";
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            orderList = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2(5, 60);
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
