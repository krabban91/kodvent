package krabban91.kodvent.kodvent.y2015.d11;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day11Test {

    @Autowired
    Day11 day11;

    @Test
    public void examples() {
        assertThat(day11.nextPassword("abcdefgh")).isEqualTo("abcdffaa");
        assertThat(day11.nextPassword("ghijklmn")).isEqualTo("ghjaabcc");
    }

    @Test
    public void increment() {
        assertThat(day11.increment("ghijklmn")).isEqualTo("ghijklmo");
        assertThat(day11.increment("zzzz")).isEqualTo("aaaa");
    }
    @Test
    public void validation() {
        assertThat(day11.isValid("abcdffaa")).isTrue();
        assertThat(day11.isValid("ghjaabcc")).isTrue();
        assertThat(day11.isValid("hijklmmn")).isFalse();
        assertThat(day11.isValid("abbceffg")).isFalse();
        assertThat(day11.isValid("abbcegjk")).isFalse();
    }

    @Test
    public void getPart1() {
        String part1 = day11.getPart1(day11.in);
        assertThat(part1).isEqualTo("cqjxxyzz");
    }

    @Test
    public void getPart2() {
        String part2 = day11.getPart2(day11.in);
        assertThat(part2).isEqualTo("cqkaabcc");
    }
}