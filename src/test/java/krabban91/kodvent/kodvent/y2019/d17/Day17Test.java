package krabban91.kodvent.kodvent.y2019.d17;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day17Test {

    @Autowired
    Day17 day17;

    @Test
    public void getPart1() throws InterruptedException {
        assertThat(day17.getPart1()).isEqualTo(-1);
    }

    @Test
    public void getPart2() throws InterruptedException {
        assertThat(day17.getPart2()).isEqualTo(-1);
    }
}
