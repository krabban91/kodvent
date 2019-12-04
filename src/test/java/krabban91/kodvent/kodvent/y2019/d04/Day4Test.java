package krabban91.kodvent.kodvent.y2019.d04;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day4Test {

    @Autowired
    Day4 day4;

@Test
public void increase(){
    assertThat(day4.alwaysIncreasing(111123)).isEqualTo(true);
    assertThat(day4.alwaysIncreasing(135679)).isEqualTo(true);
    assertThat(day4.alwaysIncreasing(132679)).isEqualTo(false);

}
    @Test
    public void twin(){
        assertThat(day4.hasTwin(135679)).isEqualTo(false);
        assertThat(day4.hasTwin(111679)).isEqualTo(true);
        assertThat(day4.hasTwin(111122)).isEqualTo(true);
        assertThat(day4.hasTwin(111123)).isEqualTo(true);
    }
    @Test
    public void strictTwin(){
        assertThat(day4.hasStrictTwin(135679)).isEqualTo(false);
        assertThat(day4.hasStrictTwin(111679)).isEqualTo(false);
        assertThat(day4.hasStrictTwin(111122)).isEqualTo(true);
        assertThat(day4.hasStrictTwin(111123)).isEqualTo(false);
    }


    @Test
    public void getPart1() {
    assertThat(day4.getPart1()).isEqualTo(910);
    }

    @Test
    public void getPart2() {
        assertThat(day4.getPart2()).isEqualTo(598);
    }
}
