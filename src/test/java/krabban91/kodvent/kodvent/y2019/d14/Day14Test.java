package krabban91.kodvent.kodvent.y2019.d14;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day14Test {

    @Autowired
    Day14 day14;

    @Test
    public void getPart1() {
        day14.readInput("y2019/d14/input.txt");
        assertThat(day14.getPart1()).isEqualTo(1920219L);
    }

    @Test
    public void getPart1Example1() {
        day14.readInput("y2019/d14/example1.txt");
        assertThat(day14.getPart1()).isEqualTo(31);
    }
    @Test
    public void getPart1Example2() {
        day14.readInput("y2019/d14/example2.txt");
        assertThat(day14.getPart1()).isEqualTo(165);
    }
    @Test
    public void getPart1Example3() {
        day14.readInput("y2019/d14/example3.txt");
        assertThat(day14.getPart1()).isEqualTo(13312);
    }
    @Test
    public void getPart2Example3() {
        day14.readInput("y2019/d14/example3.txt");
        assertThat(day14.getPart2()).isEqualTo(82892753);
    }
    @Test
    public void getPart1Example4() {
        day14.readInput("y2019/d14/example4.txt");
        assertThat(day14.getPart1()).isEqualTo(180697);
    }

    @Test
    public void getPart2Example4() {
        day14.readInput("y2019/d14/example4.txt");
        assertThat(day14.getPart2()).isEqualTo(5586022);
    }
    @Test
    public void getPart1Example5() {
        day14.readInput("y2019/d14/example5.txt");
        assertThat(day14.getPart1()).isEqualTo(2210736);
    }
    @Test
    public void getPart2Example5() {
        day14.readInput("y2019/d14/example5.txt");
        assertThat(day14.getPart2()).isEqualTo(460664);
    }

    @Test
    public void getPart2() {
        day14.readInput("y2019/d14/input.txt");
        assertThat(day14.getPart2()).isEqualTo(-1);
    }
}
