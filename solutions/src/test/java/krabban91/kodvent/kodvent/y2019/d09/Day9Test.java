package krabban91.kodvent.kodvent.y2019.d09;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day9Test {

    @Autowired
    Day9 day9;

    @Test
    public void getPart1() {
        assertThat(day9.getPart1()).isEqualTo(2399197539L);
    }

    @Test
    public void getPart2() {
        assertThat(day9.getPart2()).isEqualTo(35106L);
    }
}
