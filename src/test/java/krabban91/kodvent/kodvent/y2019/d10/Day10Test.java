package krabban91.kodvent.kodvent.y2019.d10;

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
    Day10 day10;

    @Test
    public void getPart1() {
        assertThat(day10.getPart1()).isEqualTo(33);
    }

    @Test
    public void getPart2() {
        assertThat(day10.getPart2()).isEqualTo(-1);
    }
}
