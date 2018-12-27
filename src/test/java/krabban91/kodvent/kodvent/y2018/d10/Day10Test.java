package krabban91.kodvent.kodvent.y2018.d10;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day10Test {

    @Autowired
    private Day10 day10;

    @Test
    public void getPart1() {

        day10.readInput("y2018/d10/input.txt");
        assertThat(day10.getPart1()).isEqualTo("HI");
    }

    @Test
    public void getPart2() {

        day10.readInput("y2018/d10/input.txt");
        day10.getPart1();
        assertThat(day10.getPart2()).isEqualTo(3);
    }
}