package krabban91.kodvent.kodvent.y2018.d15;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day15Test {

    @Autowired
    private Day15 day15;

    @Test
    public void getPart1Debug() {
        day15.readInput("y2018/d15/extra.txt");
    }

    @Test
    public void getPart1example6() {
        day15.readInput("y2018/d15/extra6.txt");
        assertThat(day15.getPart1()).isEqualTo(27730);
    }

    @Test
    public void getPart1example1() {
        day15.readInput("y2018/d15/input.txt");
        assertThat(day15.getPart1()).isEqualTo(36334);
    }

    @Test
    public void getPart1Example2() {
        day15.readInput("y2018/d15/extra2.txt");
        assertThat(day15.getPart1()).isEqualTo(39514);
    }

    @Test
    public void getPart1Example3() {
        day15.readInput("y2018/d15/extra3.txt");
        assertThat(day15.getPart1()).isEqualTo(27755);
    }

    @Test
    public void getPart1Example4() {
        day15.readInput("y2018/d15/extra4.txt");
        assertThat(day15.getPart1()).isEqualTo(28944);
    }

    @Test
    public void getPart1Example5() {
        day15.readInput("y2018/d15/extra5.txt");
        assertThat(day15.getPart1()).isEqualTo(18740);
    }

    @Test
    public void getPart2Example2() {
        String path = "y2018/d15/extra2.txt";
        day15.readInput(path);
        assertThat(day15.getPart2(path)).isEqualTo(31284);
    }

    @Test
    public void getPart2Example3() {
        String path = "y2018/d15/extra3.txt";
        day15.readInput(path);
        assertThat(day15.getPart2(path)).isEqualTo(3478);
    }

    @Test
    public void getPart2Example4() {
        String path = "y2018/d15/extra4.txt";
        day15.readInput(path);
        assertThat(day15.getPart2(path)).isEqualTo(6474);
    }

    @Test
    public void getPart2Example5() {
        String path = "y2018/d15/extra5.txt";
        day15.readInput(path);
        assertThat(day15.getPart2(path)).isEqualTo(1140);
    }

    @Test
    public void getPart2example6() {
        String path = "y2018/d15/extra6.txt";
        day15.readInput(path);
        assertThat(day15.getPart2(path)).isEqualTo(4988);
    }
}