package krabban91.kodvent.kodvent.day18;

import krabban91.kodvent.kodvent.day13.Day13;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day18Test {

    private static String inputPath = "day18.txt";

    @Autowired
    private Day18 day18;

    @Test
    public void getPart1() {
        assertThat(day18.getPart1()).isEqualTo(1147);
    }
/*
    @Test
    public void getPart2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath2).getFile().getPath()))) {
            day13.in = day13.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(day18.getPart2()).isEqualTo(-1);
    }
    */
}