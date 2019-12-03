package krabban91.kodvent.kodvent.y2019.d03;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day3Test {

    @Autowired
    Day3 day3;


    @Test
    public void getPart1() {
        assertThat(day3.getPart1()).isEqualTo(-1);
    }

    @Test
    public void getPart2() {
        assertThat(day3.getPart2()).isEqualTo(-1);
    }
}
