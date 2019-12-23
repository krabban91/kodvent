package krabban91.kodvent.kodvent.y2019.d22;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


public class DealingActionTest {

    @Test
    public void handle() {
        List<Long> input = LongStream.range(0, 10).boxed().collect(Collectors.toList());
        assertThat(new DealingAction("deal with increment 3").handle(input)).isEqualTo(Arrays.asList(0, 7, 4, 1, 8, 5, 2, 9, 6, 3));
        assertThat(new DealingAction("cut -4").handle(input)).isEqualTo(Arrays.asList(6, 7, 8, 9, 0, 1, 2, 3, 4, 5));
        assertThat(new DealingAction("cut 3").handle(input)).isEqualTo(Arrays.asList(3, 4, 5, 6, 7, 8, 9, 0, 1, 2));
        assertThat(new DealingAction("deal into new stack").handle(input)).isEqualTo(Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1, 0));

    }
}