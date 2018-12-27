package krabban91.kodvent.kodvent.y2018.d06;

import krabban91.kodvent.kodvent.utilities.Distances;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoordinatePickerTest {

    private List<Point> locations;

    @Autowired
    private CoordinatePicker coordinatePicker;

    @Before
    public void setUp(){
        locations = coordinatePicker.getLocations();
    }

    @Test
    public void getPart1() {
        int part1 = coordinatePicker.getPart1();
        assertThat(part1).isEqualTo(17);
    }

    @Test
    public void getPart2() {
        int part2 = coordinatePicker.getPart2(32);
        assertThat(part2).isEqualTo(16);
    }

    @Test
    public void hasInfinateArea() {
        assertThat(coordinatePicker.hasInfinateArea(locations.get(0))).isTrue();
        assertThat(coordinatePicker.hasInfinateArea(locations.get(1))).isTrue();
        assertThat(coordinatePicker.hasInfinateArea(locations.get(2))).isTrue();
        assertThat(coordinatePicker.hasInfinateArea(locations.get(5))).isTrue();
        assertThat(coordinatePicker.hasInfinateArea(locations.get(3))).isFalse();
        assertThat(coordinatePicker.hasInfinateArea(locations.get(4))).isFalse();
    }

    @Test
    public void manhattanDistance() {
        assertThat(Distances.manhattan(locations.get(0),locations.get(1))).isEqualTo(5);
        assertThat(Distances.manhattan(locations.get(5),locations.get(1))).isEqualTo(10);
    }

    @Test
    public void setUpGrid() {
       GridContainer hashMaps = coordinatePicker.setUpGrid();
        assertThat(hashMaps.getWidth()).isEqualTo(8);
        assertThat(hashMaps.getHeight()).isEqualTo(9);
    }

    @Test
    public void sumOfDistances() {
        HashMap<Point, Integer> points = new HashMap<>();
        points.put(locations.get(0),5);
        points.put(locations.get(1),6);
        points.put(locations.get(2),4);
        points.put(locations.get(3),2);
        points.put(locations.get(4),3);
        points.put(locations.get(5),10);
        assertThat(GridContainer.sumOfDistances(points)).isEqualTo(30);
    }

}
