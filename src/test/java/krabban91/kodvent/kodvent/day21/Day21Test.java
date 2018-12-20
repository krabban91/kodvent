package krabban91.kodvent.kodvent.day21;

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
public class Day21Test {

    @Autowired
    private Day21 day21;


    @Test
    public void getPart1example1() {
        assertThat(day21.getPart1()).isEqualTo(-1);
    }

    @Test
    public void getPart1example2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day21-2.txt").getFile().getPath()))) {
            day21.in = day21.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day21.getPart1()).isEqualTo(-1);
    }

    @Test
    public void getPart1Example3() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day21-3.txt").getFile().getPath()))) {
            day21.in = day21.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day21.getPart1()).isEqualTo(-1);
    }
}