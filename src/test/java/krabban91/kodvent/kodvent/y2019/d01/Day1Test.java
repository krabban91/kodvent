package krabban91.kodvent.kodvent.y2019.d01;

import krabban91.kodvent.kodvent.y2015.d01.Day1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day1Test {

    @Autowired
    Day1 day1;

    @Test
    public void getPart1() {
        long part1 = day1.getPart1();
        assertThat(part1).isEqualTo(-1);
    }

    @Test
    public void getPart2() {
        int part2 = day1.getPart2();
        assertThat(part2).isEqualTo(5);
    }

}