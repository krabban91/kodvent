package krabban91.kodvent.kodvent.y2018.d07;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day7Test {

    @Autowired
    private Day7 day7;

    @Test
    public void getPart1() {
        String part1 = day7.getPart1();
        assertThat(part1).isEqualTo("CABDFE");
        assertThat(part1).isEqualTo("CABDFE");
    }

    @Test
    public void getPart2() {
        int part2 = day7.getPart2(2, 0);
        assertThat(part2).isEqualTo(15);
    }

    @Test
    public void getCostForJob() {
        int cost = day7.getCostForJob('A', 0);
        assertThat(cost).isEqualTo(1);

        cost = day7.getCostForJob('A', 60);
        assertThat(cost).isEqualTo(61);

        cost = day7.getCostForJob('Z', 60);
        assertThat(cost).isEqualTo(86);
    }
}
