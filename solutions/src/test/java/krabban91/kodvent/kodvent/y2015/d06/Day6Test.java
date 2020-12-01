package krabban91.kodvent.kodvent.y2015.d06;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day6Test {

    @Autowired
    Day6 day6;

    @Test
    public void getPart1() {
        day6.readInput("y2015/d06/input.txt");
        long part1 = day6.getPart1();
        assertThat(part1).isEqualTo(998996L);
    }
    @Test
    public void getPart1Extra() {
        day6.readInput("y2015/d06/extra.txt");
        long part1 = day6.getPart1();
        assertThat(part1).isEqualTo(999999);
    }
    @Test
    public void getPart2() {
        day6.readInput("y2015/d06/input.txt");
        long part1 = day6.getPart2();
        assertThat(part1).isEqualTo(1001996);
    }
    @Test
    public void getPart2Extra() {
        day6.readInput("y2015/d06/extra.txt");
        long part1 = day6.getPart2();
        assertThat(part1).isEqualTo(2000001);
    }
}