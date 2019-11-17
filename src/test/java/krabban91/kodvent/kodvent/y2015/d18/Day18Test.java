package krabban91.kodvent.kodvent.y2015.d18;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day18Test {

    @Autowired
    Day18 day18;

    @Test
    public void runGIF() {
        assertThat(day18.runGIF(day18.getNewLights(), 4, false)).isEqualTo(4);
        assertThat(day18.runGIF(day18.getNewLights(), 5, true)).isEqualTo(19);

    }
}