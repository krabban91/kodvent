package krabban91.kodvent.kodvent.day25;

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
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day25.txt").getFile().getPath()))) {
            day25.in = day25.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day25.getPart1()).isEqualTo(2);
    }

    @Test
    public void getPart1example2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day25-2.txt").getFile().getPath()))) {
            day25.in = day25.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day25.getPart1()).isEqualTo(4);
    }

    @Test
    public void getPart1Example3() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day25-3.txt").getFile().getPath()))) {
            day25.in = day25.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        day25.in.countConstellations();
        assertThat(day25.getPart1()).isEqualTo(3);
    }

    @Test
    public void getPart1Example4() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day25-4.txt").getFile().getPath()))) {
            day25.in = day25.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        day25.in.countConstellations();
        assertThat(day25.getPart1()).isEqualTo(8);
    }
    @Test
    public void getPart2() {
        assertThat(day25.getPart2()).isEqualTo(51);
    }
}