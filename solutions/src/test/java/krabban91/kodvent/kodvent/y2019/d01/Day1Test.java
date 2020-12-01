package krabban91.kodvent.kodvent.y2019.d01;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day1Test {

    @Autowired
    Day1 day1;

    @Test
    public void getPart1() {
        assertThat(day1.getPart1(Collections.singletonList(12L))).isEqualTo(2);
        assertThat(day1.getPart1(Collections.singletonList(14L))).isEqualTo(2);
        assertThat(day1.getPart1(Collections.singletonList(1969L))).isEqualTo(654);
        assertThat(day1.getPart1(Collections.singletonList(100756L))).isEqualTo(33583);
    }

    @Test
    public void getPart2() {
        assertThat(day1.getPart2(Collections.singletonList(12L))).isEqualTo(2);
        assertThat(day1.getPart2(Collections.singletonList(1969L))).isEqualTo(966);
        assertThat(day1.getPart2(Collections.singletonList(100756L))).isEqualTo(50346);
    }

}