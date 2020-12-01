package krabban91.kodvent.kodvent.y2018.d20;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day20Test {

    @Autowired
    private Day20 day20;

    @Test
    public void getPart1example1() {
        day20.readInput("y2018/d20/input.txt");
        assertThat(day20.getPart1()).isEqualTo(18);
    }

    @Test
    public void getPart1example2() {
        day20.readInput("y2018/d20/extra.txt");
        assertThat(day20.getPart1()).isEqualTo(23);
    }

    @Test
    public void getPart1Example3() {
        day20.readInput("y2018/d20/extra2.txt");
        assertThat(day20.getPart1()).isEqualTo(31);
    }

    @Test
    public void getPart1exampleSmall1() {
        day20.readInput("y2018/d20/extraSmall.txt");
        assertThat(day20.getPart1()).isEqualTo(3);
    }

    @Test
    public void getPart1exampleSmall2() {
        day20.readInput("y2018/d20/extraSmall2.txt");
        assertThat(day20.getPart1()).isEqualTo(10);
    }

    @Test
    public void getPart2example1() {
        day20.readInput("y2018/d20/input.txt");
        assertThat(day20.getPart2()).isEqualTo(0);
    }
}