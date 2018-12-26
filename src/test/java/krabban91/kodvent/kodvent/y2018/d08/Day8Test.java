package krabban91.kodvent.kodvent.y2018.d08;

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
public class Day8Test {

    @Autowired
    private Day8 day8;

    @Test
    public void getPart1() {
        assertThat(day8.getPart1()).isEqualTo(138);
    }

    @Test
    public void getPart2() {
        assertThat(day8.getPart2()).isEqualTo(66);
    }

    @Test
    public void readInput() {
        String inputPath = "y2018/d08/input.txt";
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            LicenseTreeNode rows = day8.readInput(stream);
            assertThat(rows.children.size()).isEqualTo(2);
            assertThat(rows.metadata.size()).isEqualTo(3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}