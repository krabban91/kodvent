package krabban91.kodvent.kodvent.y2019.d08;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day8Test {

    @Autowired
    Day8 day8;

    @Test
    public void getPart1() {
        assertThat(day8.getPart1()).isEqualTo(1572);
    }

    @Test
    public void getPart2() {
        assertThat(day8.getPart2()).isEqualTo(
                "*  * *   **  * **** **** \n" +
                "* *  *   **  * *    *    \n" +
                "**    * * **** ***  ***  \n" +
                "* *    *  *  * *    *    \n" +
                "* *    *  *  * *    *    \n" +
                "*  *   *  *  * *    **** \n");
    }
}
