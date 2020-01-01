package krabban91.kodvent.kodvent.y2019.d22;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day22Test {

    @Autowired
    Day22 day22;

    @Test
    public void getPart1() throws InterruptedException {
        assertThat(day22.getPart1()).isEqualTo(5169);
    }

    @Test
    public void oldGetPart1() throws InterruptedException {
        assertThat(day22.oldGetPart1()).isEqualTo(5169);
    }

    @Test
    public void getPart2() throws InterruptedException {
        assertThat(day22.getPart2()).isEqualTo(74258074061935L);
    }
}
