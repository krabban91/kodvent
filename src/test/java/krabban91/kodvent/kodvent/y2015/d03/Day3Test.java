package krabban91.kodvent.kodvent.y2015.d03;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day3Test {

    @Autowired
    Day3 day3;

    @Test
    public void getPart1() {
        day3.readInput("y2015/d03/input.txt");
        long part1 = day3.getPart1();
        assertThat(part1).isEqualTo(2);
    }
    @Test
    public void getPart1Extra() {
        day3.readInput("y2015/d03/extra.txt");
        long part1 = day3.getPart1();
        assertThat(part1).isEqualTo(4);
    }
    @Test
    public void getPart1Extra2() {
        day3.readInput("y2015/d03/extra2.txt");
        long part1 = day3.getPart1();
        assertThat(part1).isEqualTo(2);
    }

    @Test
    public void getPart2() {
        day3.readInput("y2015/d03/input.txt");
        int part2 = day3.getPart2();
        assertThat(part2).isEqualTo(3);
    }


    @Test
    public void getPart2Extra() {
        day3.readInput("y2015/d03/extra.txt");
        int part2 = day3.getPart2();
        assertThat(part2).isEqualTo(3);
    }
    @Test
    public void getPart2Extra2() {
        day3.readInput("y2015/d03/extra2.txt");
        int part2 = day3.getPart2();
        assertThat(part2).isEqualTo(11);
    }
}