package krabban91.kodvent.kodvent.y2018.d09;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CircularListTest {

    private CircularList list;

    @Before
    public void before() {
        list = new CircularList();
        list.addFirst(0);
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);
        list.addFirst(4);
    }

    @Test
    public void rotate0() {
        list.rotate(0);
        assertThat(list.removeFirst()).isEqualTo(4);
    }

    @Test
    public void rotate2() {
        list.rotate(2);
        assertThat(list.removeFirst()).isEqualTo(2);
    }
    @Test
    public void rotate1() {
        list.rotate(1);
        assertThat(list.removeFirst()).isEqualTo(3);
    }

    @Test
    public void rotateMinus7() {
        list.rotate(7);
        assertThat(list.removeFirst()).isEqualTo(2);
    }
}
