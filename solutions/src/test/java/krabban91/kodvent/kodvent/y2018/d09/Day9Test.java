package krabban91.kodvent.kodvent.y2018.d09;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day9Test {

    @Autowired
    private Day9 day9;

    @Test
    public void getPart1() {
        day9.setRules(new MarbleRules(9, 25));
        day9.setDebug(true);
        assertThat(day9.getPart1()).isEqualTo(32);
    }

    @Test
    public void example2() {
        day9.setRules(new MarbleRules(10, 1618));
        assertThat(day9.getPart1()).isEqualTo(8317);

    }

    @Test
    public void example3() {
        day9.setRules(new MarbleRules(13, 7999));
        assertThat(day9.getPart1()).isEqualTo(146373);
    }

    @Test
    public void example4() {
        day9.setRules(new MarbleRules(17, 1104));
        assertThat(day9.getPart1()).isEqualTo(2764);
    }

    @Test
    public void example5() {
        day9.setRules(new MarbleRules(21, 6111));
        assertThat(day9.getPart1()).isEqualTo(54718);
    }

    @Test
    public void example6() {
        day9.setRules(new MarbleRules(30, 5807));
        assertThat(day9.getPart1()).isEqualTo(37305);
    }

    @Test
    public void redditExample1() {
        day9.setRules(new MarbleRules(9, 48));
        assertThat(day9.getPart1()).isEqualTo(63);
    }

    @Test
    public void redditExample2() {
        day9.setRules(new MarbleRules(1, 48));
        assertThat(day9.getPart1()).isEqualTo(95);
    }
}
