package krabban91.kodvent.kodvent.y2015.d14;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day14Test {

    @Autowired
    Day14 day14;

    @Test
    public void getPart1() {
        long part1 = day14.getPart1();
        assertThat(part1).isEqualTo(2655);
    }

    @Test
    public void getPart2() {
        long part2 = day14.getPart2();
        assertThat(part2).isEqualTo(1059);
    }
}