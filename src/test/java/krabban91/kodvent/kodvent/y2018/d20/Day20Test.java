package krabban91.kodvent.kodvent.y2018.d20;

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

        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("y2018/d20/input.txt").getFile().getPath()))) {
            day20.in = day20.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(day20.getPart1()).isEqualTo(18);
    }

    @Test
    public void getPart1example2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("y2018/d20/extra.txt").getFile().getPath()))) {
            day20.in = day20.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day20.getPart1()).isEqualTo(23);
    }

    @Test
    public void getPart1Example3() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("y2018/d20/extra2.txt").getFile().getPath()))) {
            day20.in = day20.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day20.getPart1()).isEqualTo(31);
    }

    @Test
    public void getPart1exampleSmall1() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("y2018/d20/extraSmall.txt").getFile().getPath()))) {
            day20.in = day20.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day20.getPart1()).isEqualTo(3);
    }


    @Test
    public void getPart1exampleSmall2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("y2018/d20/extraSmall2.txt").getFile().getPath()))) {
            day20.in = day20.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day20.getPart1()).isEqualTo(10);
    }

    @Test
    public void getPart2example1() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("y2018/d20/input.txt").getFile().getPath()))) {
            day20.in = day20.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day20.getPart2()).isEqualTo(0);
    }


}