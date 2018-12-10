package krabban91.kodvent.kodvent.day11;

import javafx.geometry.Point3D;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class FuelCellGrid {

    private Map<Point, Integer> grid;
    private int serialNumber;

    public FuelCellGrid(String serialNumber) {
        this(Integer.parseInt(serialNumber));
    }

    public FuelCellGrid(int serialNumber) {
        this.serialNumber = serialNumber;
        grid = getInitialGrid(serialNumber);
    }

    public Map.Entry<Point, Integer> gethighestValueFuelCell() {
        Map<Point, Integer> scoreGrid = new HashMap<>();
        IntStream.range(1, 299).forEach(x -> IntStream.range(1, 299).forEach(y -> scoreGrid.put(new Point(x, y), getFuelCellScore(x, y, 3))));
        return scoreGrid.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
    }

    public Map.Entry<Point3D, Integer> gethighestValueFuelCellWithVariableWith() {
        Map<Point3D, Integer> scoreGrid = new HashMap<>();
        IntStream.range(1, 301).forEach(z -> {
            IntStream.range(1, 301 - z + 1).forEach(x ->
                    IntStream.range(1, 301 - z + 1).forEach(y ->
                            scoreGrid.put(new Point3D(x, y, z), getFuelCellScore(x, y, z))));
            System.out.println(scoreGrid.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get());
        });
        return scoreGrid.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
    }

    private int getFuelCellScore(int x, int y, int size) {
        AtomicInteger sum = new AtomicInteger(0);
        IntStream.range(x, x + size).forEach(cx -> IntStream.range(y, y + size).forEach(cy -> sum.addAndGet(grid.get(new Point(cx, cy)))));
        return sum.get();
    }

    private static Map<Point, Integer> getInitialGrid(int serialNumber) {
        HashMap<Point, Integer> grid = new HashMap<>();
        IntStream.range(1, 301).forEach(x -> IntStream.range(1, 301).forEach(y -> {
            Point point = new Point(x, y);
            Integer rackId = x + 10;
            grid.put(point, getGridValue(point, serialNumber));
        }));
        return grid;
    }

    private static int getRackId(Point point) {
        return point.x + 10;
    }

    private static int addValue(int value, int extra) {
        return value + extra;
    }

    private static Integer hundredsValue(Integer value) {
        return (value / 100) % 10;
    }

    private static int addSerialNumber(int value, int serialNumber) {
        return addValue(value, serialNumber);
    }

    private static int multiplyByRackId(Point point, int value) {
        return value * getRackId(point);
    }

    private static int getGridValue(Point point, int serialNumber) {
        return addValue(
                hundredsValue(
                        multiplyByRackId(
                                point,
                                addSerialNumber(
                                        multiplyByRackId(
                                                point,
                                                point.y),
                                        serialNumber))),
                -5);

    }

}
