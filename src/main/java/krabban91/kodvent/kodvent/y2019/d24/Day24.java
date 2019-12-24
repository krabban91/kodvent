package krabban91.kodvent.kodvent.y2019.d24;

import krabban91.kodvent.kodvent.utilities.Grid;
import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.logging.LogUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day24 {
    List<List<BugTile>> in;

    public Day24() {
        System.out.println("::: Starting Day 24 :::");
        String inputPath = "y2019/d24/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        Map<List<List<BugTile>>, Long> history = new HashMap<>();
        List<List<BugTile>> current = in;
        long minute = 0;
        while (!history.containsKey(current)){
            System.out.println("Minute "+minute);

            System.out.println(LogUtils.tiles(current));
            history.put(current, minute);
            minute++;
            current = nextState(current, minute);
        }
        System.out.println("Match");
        System.out.println(LogUtils.tiles(current));

        List<List<BugTile>> finalCurrent1 = current;
        List<List<Long>> collect = IntStream.range(0, current.size()).mapToObj(y -> IntStream.range(0, finalCurrent1.get(y).size()).mapToObj(x -> (long) ((finalCurrent1.get(y).get(x).isBug() ? 1 : 0) * Math.pow(2, (y * finalCurrent1.size() + x)))).collect(Collectors.toList())).collect(Collectors.toList());
        return collect.stream().mapToLong(l-> l.stream().mapToLong(e->e).sum()).sum();
    }

    private List<List<BugTile>> nextState(List<List<BugTile>> current, long minute) {
        Grid<BugTile> grid = new Grid<>(current);
        return IntStream.range(0,current.size()).mapToObj(y-> IntStream.range(0, current.get(y).size()).mapToObj(x-> current.get(y).get(x).nextState(x,y, minute,  grid.getAdjacentTiles(y,x))).collect(Collectors.toList())).collect(Collectors.toList());
    }

    public long getPart2() {
        return -1;
    }

    public void readInput(String inputPath) {
        in = Input.getLines(inputPath).stream().map(s->s.chars().mapToObj(c-> new BugTile((char)c =='#')).collect(Collectors.toList())).collect(Collectors.toList());
    }
}
