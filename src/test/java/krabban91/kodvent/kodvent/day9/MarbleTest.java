package krabban91.kodvent.kodvent.day9;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MarbleTest {

    @Test
    public void nextMarbleIndex() {
        assertThat(Marble.nextMarbleIndex(0, 2, 2)).isEqualTo(2);
    }

    @Test
    public void nextUnder() {
        assertThat(Marble.nextMarbleIndex(0, -2, 2)).isEqualTo(2);
    }

    @Test
    public void nextMarble15() {
        assertThat(Marble.nextMarbleIndex(15, 2, 16)).isEqualTo(1);
    }

    @Test
    public void nextMarble23() {
        assertThat(Marble.nextMarbleIndex(13, -7, 23)).isEqualTo(6);
    }
}
