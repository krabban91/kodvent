package krabban91.kodvent.kodvent.y2015.d12;

import krabban91.kodvent.kodvent.y2015.d11.Day11;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day12Test {

    @Autowired
    Day12 day12;

    @Test
    public void examples() {
        assertThat(day12.sumOfNumbers("[1,2,3]", false)).isEqualTo(6);
        assertThat(day12.sumOfNumbers("{\"a\":2,\"b\":4}", false)).isEqualTo(6);
        assertThat(day12.sumOfNumbers("[[[3]]]", false)).isEqualTo(3);
        assertThat(day12.sumOfNumbers("{\"a\":{\"b\":4},\"c\":-1}", false)).isEqualTo(3);
        assertThat(day12.sumOfNumbers("[]", false)).isEqualTo(0);
        assertThat(day12.sumOfNumbers("{}", false)).isEqualTo(0);
    }
    @Test
    public void examples2() {
        assertThat(day12.sumOfNumbers("[1,2,3]", true)).isEqualTo(6);
        assertThat(day12.sumOfNumbers("{\"a\":2,\"b\":4}", true)).isEqualTo(6);
        assertThat(day12.sumOfNumbers("[[[3]]]", true)).isEqualTo(3);
        assertThat(day12.sumOfNumbers("{\"a\":{\"b\":4},\"c\":-1}", true)).isEqualTo(3);
        assertThat(day12.sumOfNumbers("[]", true)).isEqualTo(0);
        assertThat(day12.sumOfNumbers("{}", true)).isEqualTo(0);

        assertThat(day12.sumOfNumbers("[1,{\"c\":\"red\",\"b\":2},3]", true)).isEqualTo(4);
        assertThat(day12.sumOfNumbers("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}", true)).isEqualTo(0);
        assertThat(day12.sumOfNumbers("[1,\"red\",5]", true)).isEqualTo(6);
    }


    @Test
    public void getPart1() {
        long part1 = day12.getPart1();
        assertThat(part1).isEqualTo(191164L);
    }

    @Test
    public void getPart2() {
        long part2 = day12.getPart2();
        assertThat(part2).isEqualTo(87842L);
    }
}