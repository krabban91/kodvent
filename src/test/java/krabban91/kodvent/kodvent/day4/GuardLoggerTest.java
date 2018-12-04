package krabban91.kodvent.kodvent.day4;

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
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GuardLoggerTest {

    private static String inputPath = "day4.txt";

    @Autowired
    GuardLogger guardLogger;

    @Test
    public void getPart1() {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            int part1 = guardLogger.getPart1(stream);
            assertThat(part1).isEqualTo(240);
        } catch (IOException e) {
            e.printStackTrace();
            fail("exception on io.");
        }
    }
}