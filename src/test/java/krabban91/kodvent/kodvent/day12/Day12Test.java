package krabban91.kodvent.kodvent.day12;

import javafx.geometry.Point3D;
import krabban91.kodvent.kodvent.day11.Day11;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day12Test {

    @Autowired
    private Day12 day12;
    @Test
    public void getPart1() {
        assertThat(day12.getPart1()).isEqualTo(325);
    }

    @Test
    public void getPart2Example0() {
        assertThat(day12.getPart2()).isEqualTo(-1);
    }

    @Test
    public void getPart2Example1() {
        assertThat(day12.getPart2()).isEqualTo(-1);
    }
}