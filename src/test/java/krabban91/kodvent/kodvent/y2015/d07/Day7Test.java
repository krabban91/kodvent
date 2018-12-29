package krabban91.kodvent.kodvent.y2015.d07;

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
    Day7 day7;

    @Test
    public void getPart1() {
        BobbyTablesKit.setDebug(true);
        long part1 = day7.getPart1();
        assertThat(part1).isEqualTo(65412);
    }

    @Test
    public void getD() {
        BobbyTablesKit.setDebug(true);
        long part1 = day7.in.getValueOf("d");
        assertThat(part1).isEqualTo(72);
    }

    @Test
    public void getE() {
        BobbyTablesKit.setDebug(true);
        long part1 = day7.in.getValueOf("e");
        assertThat(part1).isEqualTo(507);
    }

    @Test
    public void getF() {
        BobbyTablesKit.setDebug(true);
        long part1 = day7.in.getValueOf("f");
        assertThat(part1).isEqualTo(492);
    }

    @Test
    public void getG() {
        BobbyTablesKit.setDebug(true);
        long part1 = day7.in.getValueOf("g");
        assertThat(part1).isEqualTo(114);
    }

    @Test
    public void getH() {
        BobbyTablesKit.setDebug(true);
        long part1 = day7.in.getValueOf("a");
        assertThat(part1).isEqualTo(65412);
    }

    @Test
    public void getI() {
        BobbyTablesKit.setDebug(true);
        long part1 = day7.in.getValueOf("i");
        assertThat(part1).isEqualTo(65079);
    }

    @Test
    public void getX() {
        BobbyTablesKit.setDebug(true);
        long part1 = day7.in.getValueOf("x");
        assertThat(part1).isEqualTo(123);
    }

    @Test
    public void getY() {
        BobbyTablesKit.setDebug(true);
        long part1 = day7.in.getValueOf("y");
        assertThat(part1).isEqualTo(456);
    }
}