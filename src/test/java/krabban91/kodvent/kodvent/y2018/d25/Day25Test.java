package krabban91.kodvent.kodvent.y2018.d25;

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
public class Day25Test {

    @Autowired
    private Day25 day25;

    @Test
    public void getPart1() {
        day25.readInput("y2018/d25/input.txt");
        assertThat(day25.getPart1()).isEqualTo(2);
    }

    @Test
    public void getPart1example2() {
        day25.readInput("y2018/d25/extra.txt");
        assertThat(day25.getPart1()).isEqualTo(4);
    }

    @Test
    public void getPart1Example3() {
        day25.readInput("y2018/d25/extra2.txt");
        assertThat(day25.getPart1()).isEqualTo(3);
    }

    @Test
    public void getPart1Example4() {
        day25.readInput("y2018/d25/extra3.txt");
        assertThat(day25.getPart1()).isEqualTo(8);
    }
}