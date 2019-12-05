package krabban91.kodvent.kodvent.y2019.d05;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day5Test {

    @Autowired
    Day5 day5;
    
    @Test
    public void getPart1() {
        assertThat(day5.getPart1()).isEqualTo(4511442);
    }

    @Test
    public void getPart2() {
        assertThat(day5.getPart2()).isEqualTo(12648139);
    }
}
