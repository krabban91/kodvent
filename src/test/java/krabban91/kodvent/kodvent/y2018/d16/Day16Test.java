package krabban91.kodvent.kodvent.y2018.d16;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day16Test {
    private static String inputPath2 = "day14.txt";

    @Autowired
    private Day16 day16;

    @Test
    public void getPart1Example1() {
        assertThat(day16.getPart1()).isEqualTo(-1);
    }
/*    @Test
    public void getPart1Example2() {
        d16.in = new RecipeMaker("5");
        assertThat(d16.getPart1()).isEqualTo("0124515891");
    }
    @Test
    public void getPart1Example3() {
        d16.in = new RecipeMaker("18");
        assertThat(d16.getPart1()).isEqualTo("9251071085");
    }

    @Test
    public void getPart1Example4() {
        d16.in = new RecipeMaker("2018");
        assertThat(d16.getPart1()).isEqualTo("5941429882");
    }

    @Test
    public void getPart2example1() {
        d16.in = new RecipeMaker("51589");
        assertThat(d16.getPart2()).isEqualTo(9);
    }

    @Test
    public void getPart2example2() {
        d16.in = new RecipeMaker("01245");
        assertThat(d16.getPart2()).isEqualTo(5);
    }    @Test
    public void getPart2example3() {
        d16.in = new RecipeMaker("92510");
        assertThat(d16.getPart2()).isEqualTo(18);
    }    @Test
    public void getPart2example4() {
        d16.in = new RecipeMaker("59414");
        assertThat(d16.getPart2()).isEqualTo(2018);
    }
*/
}