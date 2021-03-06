package krabban91.kodvent.kodvent.y2015.d04;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day4Test {

    @Autowired
    Day4 day4;

    @Test
    public void getPart1() {
        day4.readInput("y2015/d04/input.txt");
        long part1 = day4.getPart1();
        assertThat(part1).isEqualTo(609043);
    }
    @Test
    public void getPart1Extra() {
        day4.readInput("y2015/d04/extra.txt");
        long part1 = day4.getPart1();
        assertThat(part1).isEqualTo(1048970);
    }
}