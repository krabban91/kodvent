package krabban91.kodvent.kodvent.day3;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class FabricSlicer {

    private static String inputPath = "day3.txt";
    private List<Integer>[][] fabricOverlap = new LinkedList[1000][1000];
    private Set<Integer> triedCandidate = new HashSet<>();
    private List<Integer> santasClaimCandidate = new LinkedList<>();

    public FabricSlicer() {
        System.out.println("::: Starting Day 3:::");
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    private long getPart1() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            stream.forEach(this::mapClaim);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return calculateOverlap();
    }

    private void mapClaim(String claim) {
        Claim c = new Claim(claim);
        for (int x = c.getX(); x < c.getX() + c.getWidth(); x++) {
            for (int y = c.getY(); y < c.getY() + c.getHeight(); y++) {
                if (fabricOverlap[x][y] == null) {
                    fabricOverlap[x][y] = new LinkedList<>();
                }
                fabricOverlap[x][y].add(c.getId());
            }
        }
    }

    private long calculateOverlap() {
        return Stream.of(fabricOverlap)
                .map(col -> Stream.of(col)
                        .filter(list -> list != null && list.size() > 1)
                        .count())
                .reduce(0l, Long::sum);
    }

    private int getPart2() {
        Stream.of(fabricOverlap).forEach(col -> Stream.of(col)
                .filter(list -> list != null)
                .forEach(list -> {
                    if (list.size() == 1) {
                        if (!santasClaimCandidate.contains(list.get(0)) && !triedCandidate.contains(list.get(0))) {
                            santasClaimCandidate.add(list.get(0));
                            triedCandidate.add(list.get(0));
                        }
                    } else {
                        list.forEach(i -> {
                            santasClaimCandidate.remove(i);
                            triedCandidate.add(i);
                        });
                    }
                }));
        return santasClaimCandidate.get(0);
    }
}
