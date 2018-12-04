package krabban91.kodvent.kodvent.day1;

import krabban91.kodvent.kodvent.day3.FabricSlicer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FrequencyHandlerTest {

    @Autowired
    FrequencyHandler frequencyHandler;

    @Test
    public void getPart1() {
        int part1 = frequencyHandler.getPart1();
        assertThat(part1).isEqualTo(3);
    }

    @Test
    public void getPart2() {
        int part2 = frequencyHandler.getPart2();
        assertThat(part2).isEqualTo(2);
    }
}
