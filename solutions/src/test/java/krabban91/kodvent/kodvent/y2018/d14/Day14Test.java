package krabban91.kodvent.kodvent.y2018.d14;

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
    private Day14 day14;

    @Test
    public void getPart1Example1() {
        day14.in = new RecipeMaker("9");
        day14.in.setDebug(true);
        assertThat(day14.getPart1()).isEqualTo("5158916779");
    }

    @Test
    public void getPart1Example2() {
        day14.in = new RecipeMaker("5");
        assertThat(day14.getPart1()).isEqualTo("0124515891");
    }

    @Test
    public void getPart1Example3() {
        day14.in = new RecipeMaker("18");
        assertThat(day14.getPart1()).isEqualTo("9251071085");
    }

    @Test
    public void getPart1Example4() {
        day14.in = new RecipeMaker("2018");
        assertThat(day14.getPart1()).isEqualTo("5941429882");
    }

    @Test
    public void getPart2example1() {
        day14.in = new RecipeMaker("51589");
        assertThat(day14.getPart2()).isEqualTo(9);
    }

    @Test
    public void getPart2example2() {
        day14.in = new RecipeMaker("01245");
        assertThat(day14.getPart2()).isEqualTo(5);
    }

    @Test
    public void getPart2example3() {
        day14.in = new RecipeMaker("92510");
        assertThat(day14.getPart2()).isEqualTo(18);
    }

    @Test
    public void getPart2example4() {
        day14.in = new RecipeMaker("59414");
        assertThat(day14.getPart2()).isEqualTo(2018);
    }
}