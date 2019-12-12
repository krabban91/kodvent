package krabban91.kodvent.kodvent.y2019.d12;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Day12 {
    List<Moon> in;

    public Day12() {
        System.out.println("::: Starting Day 12 :::");
        String inputPath = "y2019/d12/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public long getPart1() {
        int steps = 0;
        final List<Moon> moons = in.stream().map((x) -> (Moon)x.clone()).collect(Collectors.toList());
        while (steps<1000){
            gravitate(moons);
            move(moons);
            steps++;
        }
        return moons.stream().mapToLong(Moon::tot).sum();
    }

    private void move(List<Moon> moons) {
        moons.forEach(Moon::move);
    }

    private void gravitate(List<Moon> moons) {
        moons.forEach(m->m.applyGravity(moons));
    }

    public long getPart2() {
        long steps = 0;
        final List<Moon> moons = in.stream().map((x) -> (Moon)x.clone()).collect(Collectors.toList());
        while (true){
            // find cycle per axis. multiply and return. 
            gravitate(moons);
            move(moons);
            steps++;

            if(isInitialState(moons)){
                return steps;
            }
        }
    }

    private boolean isInitialState(List<Moon> moons) {
        for(int i = 0; i <moons.size(); i++){
            if(!moons.get(i).equals(in.get(i))){
                return false;
            }
        }
        return true;
    }

    public void readInput(String inputPath) {

        in = Input.getLines(inputPath).stream()
                .map(Moon::new)
                .collect(Collectors.toList());
    }
}
