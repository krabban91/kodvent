package krabban91.kodvent.kodvent.y2019.d23;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day23Test {

    @Autowired
    Day23 day23;

    @Test
    public void getPart1() throws InterruptedException {
        assertThat(day23.getPart1()).isEqualTo(24268);
    }

    @Test
    public void getPart2() throws InterruptedException {
        assertThat(day23.getPart2()).isEqualTo(19316);
    }
}
