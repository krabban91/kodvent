package krabban91.kodvent.kodvent.y2019.d18;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day18Test {

    @Autowired
    Day18 day18;

    @Test
    public void getPart1() {
        day18.readInput("y2019/d18/input.txt");
        assertThat(day18.getPart1()).isEqualTo(-1);
    }
    @Test
    public void getPart1Extra1() {
        day18.readInput("y2019/d18/extra.txt");
        assertThat(day18.getPart1()).isEqualTo(8);
    }
    @Test
    public void getPart1Extra2() {
        day18.readInput("y2019/d18/extra2.txt");
        day18.debug=true;
        assertThat(day18.getPart1()).isEqualTo(86);
    }
    @Test
    public void getPart1Extra3() {
        day18.readInput("y2019/d18/extra3.txt");
        assertThat(day18.getPart1()).isEqualTo(132);
    }
    @Test
    public void getPart1Extra4() {
        day18.readInput("y2019/d18/extra4.txt");
        assertThat(day18.getPart1()).isEqualTo(136);
    }
    @Test
    public void getPart1Extra5() {
        day18.readInput("y2019/d18/extra5.txt");
        assertThat(day18.getPart1()).isEqualTo(81);
    }

    @Test
    public void getPart2() {
        day18.readInput("y2019/d18/input.txt");
        assertThat(day18.getPart2()).isEqualTo(-1);
    }
}
