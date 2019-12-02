package krabban91.kodvent.kodvent.y2019.d02;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day2Test {

    @Autowired
    Day2 day2;

    @Test
    public void getPart1() {
        assertThat(day2.getPart1(Arrays.asList(1,0,0,0,99), 0,0)).isEqualTo(2);
        assertThat(day2.getPart1(Arrays.asList(2,3,0,3,99),3,0)).isEqualTo(2);
        assertThat(day2.getPart1(Arrays.asList(1,1,1,4,99,5,6,0,99),1,1)).isEqualTo(30);
        assertThat(day2.getPart1(day2.in,1,12)).isEqualTo(682636L);
    }
    @Test
    public void getPart2() {
        assertThat(day2.getPart2()).isEqualTo(5696L);
    }
}
