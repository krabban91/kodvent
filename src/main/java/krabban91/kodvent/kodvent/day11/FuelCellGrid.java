package krabban91.kodvent.kodvent.day11;

import javafx.geometry.Point3D;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class FuelCellGrid {

    private Integer[][] grid;


    public FuelCellGrid(String serialNumber) {
        this(Integer.parseInt(serialNumber));
    }

    public FuelCellGrid(int serialNumber) {
        grid = getInitialGrid(serialNumber);
    }

    public Map.Entry<Point3D, Integer> getHighestValueFuelCellWithVariableWith(int maxWidth) {
        Integer[][][] scoreGrid = new Integer[300][300][maxWidth + 1];
        Map<Point3D, Integer> candidates = new HashMap<>();
        AtomicInteger maxScore = new AtomicInteger(0);

        IntStream.range(0, maxWidth + 1).forEach(z -> {
            IntStream.range(0, 300 - z).forEach(x ->
                    IntStream.range(0, 300 - z).forEach(y -> {
                        int cellScore = getFuelCellScore(x, y, z, scoreGrid);
                        scoreGrid[x][y][z] = cellScore;
                        if (cellScore > maxScore.get()) {
                            maxScore.set(cellScore);
                            candidates.put(new Point3D(x + 1, y + 1, z), cellScore);
                        }
                    }));
        });
        return candidates.entrySet().stream().filter(e -> e.getValue() != null).max(Comparator.comparingInt(Map.Entry::getValue)).get();
    }

    private int getFuelCellScore(int x, int y, int size, Integer[][][] scoreGrid) {
        ensureNullSafety(x, y, scoreGrid);
        if (size == 0) {
            return 0;
        }
        AtomicInteger sum = new AtomicInteger(scoreGrid[x][y][size - 1]);
        IntStream.range(x, x + size).forEach(cx -> sum.addAndGet(grid[cx][y + size - 1]));
        IntStream.range(y, y + size - 1).forEach(cy -> sum.addAndGet(grid[x + size - 1][cy]));
        return sum.get();
    }

    private void ensureNullSafety(int x, int y, Integer[][][] scoreGrid) {
        if (scoreGrid[x] == null) {
            scoreGrid[x] = new Integer[300][300];
            if (scoreGrid[x][y] == null) {
                scoreGrid[x][y] = new Integer[300];
            }
        }
    }

    // static functions assumes that cell 1,1 is stored in grid[0][0]
    private static Integer[][] getInitialGrid(int serialNumber) {
        Integer[][] grid = new Integer[300][300];
        IntStream.range(0, 300).forEach(x -> {
            if (grid[x] == null) {
                grid[x] = new Integer[300];
            }
            IntStream.range(0, 300).forEach(y -> {
                grid[x][y] = getGridValue(x, y, serialNumber);
            });
        });
        return grid;
    }

    private static int getRackId(int x) {
        return x + 1 + 10;
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

    private static int multiplyByRackId(int x, int value) {
        return value * getRackId(x);
    }

    private static int getGridValue(int x, int y, int serialNumber) {
        return addValue(
                hundredsValue(
                        multiplyByRackId(
                                x,
                                addSerialNumber(
                                        multiplyByRackId(x, y + 1),
                                        serialNumber))),
                -5);

    }

}
