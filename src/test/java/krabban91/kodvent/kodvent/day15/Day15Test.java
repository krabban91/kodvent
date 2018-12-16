package krabban91.kodvent.kodvent.day15;

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
public class Day15Test {

    @Autowired
    private Day15 day15;

    @Test
    public void getPart1Debug() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day15-debug.txt").getFile().getPath()))) {
            day15.in = day15.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPart1example6() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day15-6.txt").getFile().getPath()))) {
            day15.in = day15.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day15.getPart1()).isEqualTo(27730);
    }

    @Test
    public void getPart1example1() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day15.txt").getFile().getPath()))) {
            day15.in = day15.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day15.getPart1()).isEqualTo(36334);
    }

    @Test
    public void getPart1Example2() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day15-2.txt").getFile().getPath()))) {
            day15.in = day15.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day15.getPart1()).isEqualTo(39514);
    }

    @Test
    public void getPart1Example3() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day15-3.txt").getFile().getPath()))) {
            day15.in = day15.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day15.getPart1()).isEqualTo(27755);
    }

    @Test
    public void getPart1Example4() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day15-4.txt").getFile().getPath()))) {
            day15.in = day15.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day15.getPart1()).isEqualTo(28944);
    }

    @Test
    public void getPart1Example5() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource("day15-5.txt").getFile().getPath()))) {
            day15.in = day15.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(day15.getPart1()).isEqualTo(18740);
    }

    @Test
    public void getPart2() {
        assertThat(day15.getPart2()).isEqualTo(-1);
    }
/*
    @Test
    public void getPart2example2() {
        day15.in = new RecipeMaker("01245");
        assertThat(day15.getPart2()).isEqualTo(5);
    }    @Test
    public void getPart2example3() {
        day15.in = new RecipeMaker("92510");
        assertThat(day15.getPart2()).isEqualTo(18);
    }    @Test
    public void getPart2example4() {
        day15.in = new RecipeMaker("59414");
        assertThat(day15.getPart2()).isEqualTo(2018);
    }
*/
}