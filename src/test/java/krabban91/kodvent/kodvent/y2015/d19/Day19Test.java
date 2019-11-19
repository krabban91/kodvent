package krabban91.kodvent.kodvent.y2015.d19;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day19Test {

    @Autowired
    Day19 day19;

    @Test
    public void allPotentialMolecules() {
        List<String> hoh = day19.allPotentialMolecules("HOH", day19.getTransitions());
        assertThat(hoh.size()).isEqualTo(4);
        assertThat(day19.allPotentialMolecules("HOHOHO", day19.getTransitions()).size()).isEqualTo(7);
        assertThat(day19.allPotentialMolecules("H20", Collections.singletonList(new Transition("H => OO"))).size()).isEqualTo(1);

    }
}