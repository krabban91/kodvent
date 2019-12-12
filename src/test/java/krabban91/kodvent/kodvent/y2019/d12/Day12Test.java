package krabban91.kodvent.kodvent.y2019.d12;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day12Test {

    @Autowired
    Day12 day12;

    @Test
    public void getPart1() {
        assertThat(day12.getPart1()).isEqualTo(183);
    }

    @Test
    public void getPart2Example() {
        assertThat(day12.getPart2()).isEqualTo(2772);
    }

}
