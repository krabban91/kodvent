package krabban91.kodvent.kodvent.day9;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CircularListTest {

    private CircularList list;

    @Before
    public void before() {
        list = new CircularList();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
    }

    @Test
    public void addUnder() {
        list.add(-10, 5);
        assertThat(list.indexOf(5)).isEqualTo(5);
    }

    @Test
    public void addOver() {
        list.add(10, 5);
        assertThat(list.indexOf(5)).isEqualTo(5);
    }

    @Test
    public void add() {
        list.add(2, 5);
        assertThat(list.indexOf(5)).isEqualTo(2);
    }

    @Test
    public void removeUnder() {
        assertThat(list.remove(-10)).isEqualTo(0);
    }

    @Test
    public void removeOver() {
        assertThat(list.remove(10)).isEqualTo(0);
    }

    @Test
    public void remove() {
        assertThat(list.remove(2)).isEqualTo(2);
    }

    @Test
    public void removeFirst() {
        assertThat(list.remove(0)).isEqualTo(0);
    }
}
