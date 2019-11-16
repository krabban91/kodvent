package krabban91.kodvent.kodvent.y2015.d13;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day13Test {

    @Autowired
    Day13 day13;

    @Test
    public void happinessDeltaParser() {
        HappinessDelta actual1 = new HappinessDelta("David would lose 7 happiness units by sitting next to Bob.");
        assertThat(actual1.getChange()).isEqualTo(-7);
        assertThat(actual1.getPerson()).isEqualTo("David");
        assertThat(actual1.getNextTo()).isEqualTo("Bob");
        HappinessDelta actual2 = new HappinessDelta("David would gain 41 happiness units by sitting next to Carol.");
        assertThat(actual2.getChange()).isEqualTo(41);
        assertThat(actual2.getPerson()).isEqualTo("David");
        assertThat(actual2.getNextTo()).isEqualTo("Carol");
    }

    @Test
    public void getPart1() {
        long part1 = day13.getPart1();
        assertThat(part1).isEqualTo(330);
    }

    @Test
    public void getPart2() {
        long part2 = day13.getPart2();
        assertThat(part2).isEqualTo(286L);
    }
}
