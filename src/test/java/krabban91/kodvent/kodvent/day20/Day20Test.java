package krabban91.kodvent.kodvent.day20;

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
public class Day20Test {

    @Autowired
    private Day20 day20;


    @Test
    public void getPart1example1() {
        assertThat(day20.getPart1()).isEqualTo(18);
    }

    @Test
    public void getPart1example2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day20-2.txt").getFile().getPath()))) {
            day20.in = day20.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day20.getPart1()).isEqualTo(23);
    }

    @Test
    public void getPart1Example3() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day20-3.txt").getFile().getPath()))) {
            day20.in = day20.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day20.getPart1()).isEqualTo(31);
    }

    @Test
    public void getPart1exampleSmall1() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day20-small1.txt").getFile().getPath()))) {
            day20.in = day20.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day20.getPart1()).isEqualTo(3);
    }


    @Test
    public void getPart1exampleSmall2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day20-small2.txt").getFile().getPath()))) {
            day20.in = day20.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day20.getPart1()).isEqualTo(10);
    }

}