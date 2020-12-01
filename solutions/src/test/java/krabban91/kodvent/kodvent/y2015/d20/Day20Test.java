package krabban91.kodvent.kodvent.y2015.d20;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day20Test {

    @Autowired
    Day20 day20;


    @Test
    public void countGifts() {
        assertThat(day20.countGifts(1)).isEqualTo(10);      // 1*10
        assertThat(day20.countGifts(3)).isEqualTo(40);      // 1*10+3*10
        assertThat(day20.countGifts(79)).isEqualTo(800L);   // 1*10+79*10
        assertThat(day20.countGifts(100000)).isEqualTo(2460780L);
    }

    @Test
    public void countGiftsLazyElves() {
        assertThat(day20.countGiftsLazyElves(1)).isEqualTo(11L);    // 1*11
        assertThat(day20.countGiftsLazyElves(3)).isEqualTo(44);     // 1*11+3*11
        assertThat(day20.countGiftsLazyElves(79)).isEqualTo(869L);  // 70*11
        assertThat(day20.countGiftsLazyElves(100000)).isEqualTo(2644125L);
    }


    @Test
    public void part1() {
        assertThat(day20.getPart1()).isEqualTo(665280);
    }

    @Test
    public void part2() {
        assertThat(day20.getPart2()).isEqualTo(705600);
    }
}
