package krabban91.kodvent.kodvent.y2015.d15;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day15Test {

    @Autowired
    Day15 day15;

    @Test
    public void score() {
        Map<Ingredient, Integer> collect = Map.of(
                day15.in.stream().filter(e -> e.getName().equals("Butterscotch")).findFirst().get(), 44,
                day15.in.stream().filter(e -> e.getName().equals("Cinnamon")).findFirst().get(), 56);

        long part1 = day15.score(collect);
        assertThat(part1).isEqualTo(62842880);
    }

    @Test
    public void getPart1() {
        long part1 = day15.getPart1();
        assertThat(part1).isEqualTo(62842880l);
    }

    @Test
    public void getPart2() {
        long part2 = day15.getPart2();
        assertThat(part2).isEqualTo(57600000L);
    }
}