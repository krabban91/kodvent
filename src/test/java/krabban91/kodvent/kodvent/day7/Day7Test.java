package krabban91.kodvent.kodvent.day7;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day7Test {

    private static String inputPath = "day7.txt";

    private List<Integer> rows;

    @Autowired
    private Day7 day7;

    @Before
    public void setUp() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            rows = day7.readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPart1() {
        int part1 = day7.getPart1();
        assertThat(part1).isEqualTo(-1);
    }

    @Test
    public void getPart2() {
        int part2 = day7.getPart2();
        assertThat(part2).isEqualTo(-1);
    }
}
