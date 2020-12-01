package krabban91.kodvent.kodvent.y2015.d02;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day2Test {

    @Autowired
    Day2 day2;

    @Test
    public void getPart1() {
        long part1 = day2.getPart1();
        assertThat(part1).isEqualTo(58 + 43);
    }

    @Test
    public void getPart2() {
        int part2 = day2.getPart2();
        assertThat(part2).isEqualTo(34+14);
    }
}