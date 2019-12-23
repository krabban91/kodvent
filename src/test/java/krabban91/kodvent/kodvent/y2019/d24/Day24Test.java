package krabban91.kodvent.kodvent.y2019.d24;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day24Test {

    @Autowired
    Day24 day24;

    @Test
    public void getPart1() throws InterruptedException {
        assertThat(day24.getPart1()).isEqualTo(-1);
    }

    @Test
    public void getPart2() throws InterruptedException {
        assertThat(day24.getPart2()).isEqualTo(-1);
    }
}
