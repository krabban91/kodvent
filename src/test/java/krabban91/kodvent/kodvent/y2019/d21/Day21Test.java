package krabban91.kodvent.kodvent.y2019.d21;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day21Test {

    @Autowired
    Day21 day21;

    @Test
    public void getPart1() throws InterruptedException {
        assertThat(day21.getPart1()).isEqualTo(19362259);
    }
    
    @Test
    public void getPart2() throws InterruptedException {
        assertThat(day21.getPart2()).isEqualTo(-1);
    }
}
