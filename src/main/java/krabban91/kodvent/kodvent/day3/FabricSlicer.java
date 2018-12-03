package krabban91.kodvent.kodvent.day3;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class FabricSlicer {

    private static String inputPath = "day3.txt";
    private List<Integer>[][] fabricOverlap = new LinkedList[1000][1000];

    public FabricSlicer() {

        System.out.println("::: Starting Day 3:::");
        long part1 = getPart1();
        System.out.println("::: answer to part 1:::");
        System.out.println(part1);
        String part2 = getPart2();
        System.out.println("::: answer to part 2:::");
        System.out.println(part2);
    }

    private void mapClaim(String claim){
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

    private int calculateOverlap(){
        int overlap = 0;
        for (List<Integer>[] col : fabricOverlap) {
            for (List<Integer> list : col) {
                if (list != null && list.size() > 1) {
                    overlap++;
                }
            }
        }
        return overlap;
    }

    private String getPart2() {
        return "<no answer>";

    }

    private long getPart1() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            stream.forEach(this::mapClaim);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return calculateOverlap();
    }
}
