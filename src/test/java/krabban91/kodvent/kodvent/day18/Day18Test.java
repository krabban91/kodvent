package krabban91.kodvent.kodvent.day18;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day18Test {

    @Autowired
    private Day18 day18;

    @Test
    public void getPart1() {
        assertThat(day18.getPart1()).isEqualTo(1147);
    }

    @Test
    public void getPart2() {
        assertThat(day18.getPart2()).isEqualTo(0);
    }
}