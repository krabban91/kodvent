package krabban91.kodvent.kodvent.y2019.d07;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day7Test {

    @Autowired
    Day7 day7;

    @Test
    public void getPart1() {
        assertThat(day7.getPart1()).isEqualTo(359142L);
    }

    @Test
    public void getPart2() {
        assertThat(day7.getPart2()).isEqualTo(4374895L);
    }
}
