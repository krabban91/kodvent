package krabban91.kodvent.kodvent.y2015.d08;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day8Test {

    @Autowired
    Day8 day8;

    @Test
    public void getPart1() {
        long part1 = day8.getPart1();
        assertThat(part1).isEqualTo(23 - 11);
    }

    @Test
    public void getPart2() {
        long part1 = day8.getPart2();
        assertThat(part1).isEqualTo(42 - 23);
    }
}