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

    @Autowired
    GuardLogger guardLogger;

    @Test
    public void getPart1() {
        int part1 = guardLogger.getPart1();
        assertThat(part1).isEqualTo(240);
    }

    @Test
    public void getPart2() {
        int part2 = guardLogger.getPart2();
        assertThat(part2).isEqualTo(4455);
    }
}
