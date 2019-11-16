package krabban91.kodvent.kodvent.y2015.d17;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day17Test {

    @Autowired
    Day17 day17;

    @Test
    public void waysOfTheEggNod() {
        assertThat(day17.waysOfTheEggNod(
                Arrays.asList(
                        new Container(20),
                        new Container(15),
                        new Container(10),
                        new Container(5),
                        new Container(5)),
                25,
                new ConcurrentReferenceHashMap<>(),
                new ConcurrentReferenceHashMap<>())).isEqualTo(4);
    }

    @Test
    public void bestWaysOfTheEggNod() {
        ConcurrentReferenceHashMap<List<Container>, Integer> history = new ConcurrentReferenceHashMap<>();
        day17.waysOfTheEggNod(
                Arrays.asList(
                        new Container(20),
                        new Container(15),
                        new Container(10),
                        new Container(5),
                        new Container(5)),
                25,
                new ConcurrentReferenceHashMap<>(),
                history);
        assertThat(day17.bestWaysOfTheEggNod(history)).isEqualTo(3);
    }

}