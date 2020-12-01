package krabban91.kodvent.kodvent.y2019.d20;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day20Test {

    @Autowired
    Day20 day20;

    @Test
    public void getPart1() throws InterruptedException {
        day20.readInput("y2019/d20/input.txt");
        assertThat(day20.getPart1()).isEqualTo(516);
    }
    @Test
    public void part1Examples(){
        day20.readInput("y2019/d20/example1.txt");
        assertThat(day20.getPart1()).isEqualTo(23);
        day20.readInput("y2019/d20/example2.txt");
        assertThat(day20.getPart1()).isEqualTo(58);

    }
    @Test
    public void part2Examples(){
        day20.readInput("y2019/d20/example1.txt");
        assertThat(day20.getPart2()).isEqualTo(26);
        day20.readInput("y2019/d20/example3.txt");
        assertThat(day20.getPart2()).isEqualTo(396);

    }

    @Test
    public void getPart2() throws InterruptedException {
        day20.readInput("y2019/d20/input.txt");
        assertThat(day20.getPart2()).isEqualTo(5966);
    }
}
