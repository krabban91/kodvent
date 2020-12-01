package krabban91.kodvent.kodvent.y2016.d03;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day03Test {

    @Autowired
    Day3 day3;

    @Test
    public void getPart1() {
        long part1 = day3.getPart1();
        assertThat(part1).isEqualTo(993);
    }

    @Test
    public void getPart2() {
        long part2 = day3.getPart2();
        assertThat(part2).isEqualTo(1849);
    }
}