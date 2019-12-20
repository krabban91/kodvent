package krabban91.kodvent.kodvent.y2019.d21;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day21Test {

    @Autowired
    Day21 day21;

    @Test
    public void getPart1() throws InterruptedException {
        day21.readInput("y2019/d21/input.txt");
        assertThat(day21.getPart1()).isEqualTo(-1);
    }

    @Test
    public void part1Examples() {
        /*
        day21.readInput("y2019/d21/example1.txt");
        assertThat(day21.getPart1()).isEqualTo(-1);
        day21.readInput("y2019/d21/example2.txt");
        assertThat(day21.getPart1()).isEqualTo(-1);
         */

    }

    @Test
    public void part2Examples() {
        /*
        
        day21.readInput("y2019/d21/example1.txt");
        assertThat(day21.getPart2()).isEqualTo(-1);
        day21.readInput("y2019/d21/example3.txt");
        assertThat(day21.getPart2()).isEqualTo(-1);
         */

    }

    @Test
    public void getPart2() throws InterruptedException {
        day21.readInput("y2019/d21/input.txt");
        assertThat(day21.getPart2()).isEqualTo(-1);
    }
}
