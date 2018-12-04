package krabban91.kodvent.kodvent.day3;

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
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FabricSlicerTest {

    private static String inputPath = "day3.txt";

    @Autowired
    FabricSlicer fabricSlicer;

    @Test
    public void getPart1() {
        long part1 = fabricSlicer.getPart1();
        assertThat(part1).isEqualTo(4);
    }

    @Test
    public void getPart2() {
        int part2 = fabricSlicer.getPart2();
        assertThat(part2).isEqualTo(3);
    }
}
