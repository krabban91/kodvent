package krabban91.kodvent.kodvent.day11;

import javafx.geometry.Point3D;
import krabban91.kodvent.kodvent.day10.Day10;
import org.junit.Test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day11Test {

    @Autowired
    private Day11 day11;
    @Test
    public void getPart1Example0() {
        day11.SetSerialNumber(18);
        assertThat(day11.getPart1()).isEqualTo(new Point(33,45));
    }

    @Test
    public void getPart1Example1() {
        day11.SetSerialNumber(42);
        assertThat(day11.getPart1()).isEqualTo(new Point(21,61));
    }
    @Test
    public void getPart2Example0() {
        day11.SetSerialNumber(18);
        assertThat(day11.getPart2()).isEqualTo(new Point3D(90,269,16));
    }

    @Test
    public void getPart2Example1() {
        day11.SetSerialNumber(42);
        assertThat(day11.getPart2()).isEqualTo(new Point3D(232,251,12));
    }
}