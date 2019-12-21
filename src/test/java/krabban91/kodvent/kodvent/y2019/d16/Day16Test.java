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
    public void getBasePatternIndex(){
        assertThat(day16.getBasePatternIndex(0,1,List.of(0, 1, 0, -1))).isEqualTo(2);
        assertThat(day16.getBasePatternIndex(0,0,List.of(0, 1, 0, -1))).isEqualTo(1);
        assertThat(day16.getBasePatternIndex(1,0,List.of(0, 1, 0, -1))).isEqualTo(0);
        assertThat(day16.getBasePatternIndex(1,1,List.of(0, 1, 0, -1))).isEqualTo(1);

    }

    @Test
    public void getPart1() {
        assertThat(day16.getPart1()).isEqualTo("58672132");
    }

    @Test
    public void getPart2() {
        assertThat(day16.getPart2()).isEqualTo("91689380");
    }
}
