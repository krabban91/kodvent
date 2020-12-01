package krabban91.kodvent.kodvent.y2019.d19;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day19Test {

    @Autowired
    Day19 day19;

    @Test
    public void getPart1() throws InterruptedException {
        assertThat(day19.getPart1()).isEqualTo(234);
    }

    @Test
    public void getPart2() throws InterruptedException {
        assertThat(day19.getPart2()).isEqualTo(9290812);
    }
}
