package krabban91.kodvent.kodvent.day13;

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
public class Day13Test {

    private static String inputPath1 = "day13-1.txt";
    private static String inputPath2 = "day13.txt";

    @Autowired
    private Day13 day13;

    @Test
    public void getPart1() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath1).getFile().getPath()))) {
            day13.in = day13.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day13.getPart1()).isEqualTo(new Point(7, 3));
    }

    @Test
    public void getPart2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath2).getFile().getPath()))) {
            day13.in = day13.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(day13.getPart2()).isEqualTo(new Point(6, 4));
    }
}