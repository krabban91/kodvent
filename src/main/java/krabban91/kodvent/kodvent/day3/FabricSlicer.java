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
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FabricSlicer {

    private static String inputPath = "day3.txt";
    private List<Integer>[][] fabricOverlap = new LinkedList[1000][1000];
    private Set<Integer> triedCandidate = new HashSet<>();
    private List<Integer> santasClaimCandidate = new LinkedList<>();

    public FabricSlicer() {
        System.out.println("::: Starting Day 3:::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            this.mapClaims(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public void mapClaims(Stream<String> stream) {
        stream.map(Claim::new)
                .forEach(this::mapClaim);
    }

    public long getPart1() {
        return calculateOverlap();
    }

    public int getPart2() {
        Stream.of(fabricOverlap)
                .forEach(col -> Stream.of(col)
                .filter(list -> list != null)
                .forEach(this::findClaimCandidate));
        return santasClaimCandidate.get(0);
    }

    private void mapClaim(Claim claim) {
        IntStream.range(claim.getX0(), claim.getX1()).forEach(x -> IntStream.range(claim.getY0(), claim.getY1())
                .forEach(y -> {
                    if (fabricOverlap[x][y] == null) {
                        fabricOverlap[x][y] = new LinkedList<>();
                    }
                    fabricOverlap[x][y].add(claim.getId());
                })
        );
    }

    private long calculateOverlap() {
        return Stream.of(fabricOverlap)
                .map(col -> Stream.of(col)
                        .filter(list -> list != null && list.size() > 1)
                        .count())
                .reduce(0l, Long::sum);
    }

    private void findClaimCandidate(List<Integer> list) {
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
    }
}
