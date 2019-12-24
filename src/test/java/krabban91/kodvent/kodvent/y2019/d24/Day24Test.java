package krabban91.kodvent.kodvent.y2019.d24;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day24Test {

    @Autowired
    Day24 day24;

    @Test
    public void getPart1Example() throws InterruptedException {
        day24.readInput("y2019/d24/example1.txt");
        assertThat(day24.getPart1()).isEqualTo(2129920);
    }
    @Test
    public void getPart1() throws InterruptedException {
        day24.readInput("y2019/d24/input.txt");
        assertThat(day24.getPart1()).isEqualTo(32506911);
    }

    @Test
    public void getPart2() throws InterruptedException {
        day24.readInput("y2019/d24/input.txt");
        assertThat(day24.getPart2()).isEqualTo(2025);
    }

    @Test
    public void getPart2Example() throws InterruptedException {
        day24.readInput("y2019/d24/example1.txt");
        assertThat(day24.bugsAfter(10)).isEqualTo(99);
        assertThat(day24.getPart2()).isEqualTo(1922);

    }
}
