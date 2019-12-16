package krabban91.kodvent.kodvent.y2019.d16;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day16Test {

    @Autowired
    Day16 day16;

    @Test
    public void patternForIndex(){
        assertThat(day16.patternForIndex(List.of(0, 1, 0, -1), 0).subList(0,5)).isEqualTo(List.of(1,0,-1,0,1));
        assertThat(day16.patternForIndex(List.of(0, 1, 0, -1), 1).subList(0,5)).isEqualTo(List.of(0,1,1,0,0));
        assertThat(day16.patternForIndex(List.of(0, 1, 0, -1), 2).subList(0,5)).isEqualTo(List.of(0,0,1,1,1));
        assertThat(day16.patternForIndex(List.of(0, 1, 0, -1), 3).subList(0,5)).isEqualTo(List.of(0,0,0,1,1));
        assertThat(day16.patternForIndex(List.of(0, 1, 0, -1), 4).subList(0,5)).isEqualTo(List.of(0,0,0,0,1));
        assertThat(day16.patternForIndex(List.of(0, 1, 0, -1), 5).subList(0,5)).isEqualTo(List.of(0,0,0,0,0));

    }

    @Test
    public void getPart1() throws InterruptedException {
        assertThat(day16.getPart1()).isEqualTo(-1);
    }

    @Test
    public void getPart2() throws InterruptedException {
        assertThat(day16.getPart2()).isEqualTo(-1);
    }
}
