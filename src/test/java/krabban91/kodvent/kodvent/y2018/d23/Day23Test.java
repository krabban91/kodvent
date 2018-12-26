package krabban91.kodvent.kodvent.y2018.d23;

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
public class Day23Test {

    @Autowired
    private Day23 day23;

    @Test
    public void getPart1() {

        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("y2018/d23/input.txt").getFile().getPath()))) {
            day23.in = day23.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day23.getPart1()).isEqualTo(7);
    }

    @Test
    public void getPart2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("y2018/d23/input.txt").getFile().getPath()))) {
            day23.in = day23.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day23.getPart2()).isEqualTo(3);
    }


    @Test
    public void getPart2Example2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("y2018/d23/extra.txt").getFile().getPath()))) {
            day23.in = day23.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day23.getPart2()).isEqualTo(36);
    }

}