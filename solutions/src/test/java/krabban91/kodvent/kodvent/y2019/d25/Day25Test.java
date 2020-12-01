package krabban91.kodvent.kodvent.y2019.d25;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day25Test {

    @Autowired
    Day25 day25;

    @Test
    public void getPart1() throws InterruptedException {
        assertThat(day25.getPart1()).isEqualTo("2155873288");
    }
}
