package krabban91.kodvent.kodvent.y2018.d03;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FabricSlicerTest {

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
