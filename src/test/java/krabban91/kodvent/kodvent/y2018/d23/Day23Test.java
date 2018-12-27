package krabban91.kodvent.kodvent.y2018.d23;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day23Test {

    @Autowired
    private Day23 day23;

    @Test
    public void getPart1() {
        day23.readInput("y2018/d23/input.txt");
        assertThat(day23.getPart1()).isEqualTo(7);
    }

    @Test
    public void getPart2() {
        day23.readInput("y2018/d23/input.txt");
        assertThat(day23.getPart2()).isEqualTo(3);
    }

    @Test
    public void getPart2Example2() {
        day23.readInput("y2018/d23/extra.txt");
        assertThat(day23.getPart2()).isEqualTo(36);
    }
}