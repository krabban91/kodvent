package krabban91.kodvent.kodvent.day17;

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
public class Day17Test {

    @Autowired
    private Day17 day17;

    @Test
    public void getPart1() {
        assertThat(day17.getPart1()).isEqualTo(57);
    }

    @Test
    public void getPart2() {
        assertThat(day17.getPart2()).isEqualTo(29);
    }
}