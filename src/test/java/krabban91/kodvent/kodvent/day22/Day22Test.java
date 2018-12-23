package krabban91.kodvent.kodvent.day22;

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
public class Day22Test {

    @Autowired
    private Day22 day21;

    @Test
    public void getPart1() {
        assertThat(day21.getPart1()).isEqualTo(114);
    }

    @Test
    public void getPart2() {
        assertThat(day21.getPart2()).isEqualTo(45);
    }

}