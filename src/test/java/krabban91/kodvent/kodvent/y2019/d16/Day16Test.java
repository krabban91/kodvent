package krabban91.kodvent.kodvent.y2019.d16;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day16Test {

    @Autowired
    Day16 day16;

    @Test
    public void getPart1() throws InterruptedException {
        assertThat(day16.getPart1()).isEqualTo(-1);
    }

    @Test
    public void getPart2() throws InterruptedException {
        assertThat(day16.getPart2()).isEqualTo(-1);
    }
}
