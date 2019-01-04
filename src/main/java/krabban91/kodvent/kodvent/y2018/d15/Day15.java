package krabban91.kodvent.kodvent.y2018.d15;

import krabban91.kodvent.kodvent.utilities.Input;
import org.springframework.stereotype.Component;

@Component
public class Day15 {
    CaveBattle in;

    public long getPart1() {
        return in.battleUntilItIsOver();
    }

    public long getPart2(String inputPath) {
        long elfCount = in.initialElfCount;
        int elfStrength = 4;
        int strengthIncrement = 200;
        while (true) {
            readInput(inputPath);
            System.out.println("Setting elf strength to: " + elfStrength);
            in.setElfStrength(elfStrength);
            long l = in.battleUntilItIsOverOrAnElfDies();
            if (in.countElfs() == elfCount) {
                if (strengthIncrement == 1) {
                    return l;
                } else {
                    elfStrength -= strengthIncrement;
                    strengthIncrement /= 2;
                }
            }
            elfStrength += strengthIncrement;
        }
    }

    public void readInput(String path) {
        this.in = new CaveBattle(Input.getLines(path));
    }

    public Day15() {
        System.out.println("::: Starting Day 15 :::");
        String inputPath = "y2018/d15/input.txt";
        readInput(inputPath);
       // long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        //System.out.println(part1);
        //long part2 = getPart2(inputPath);
        System.out.println(": answer to part 2 :");
        //System.out.println(part2);
    }
}
