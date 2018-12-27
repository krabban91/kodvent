package krabban91.kodvent.kodvent.y2018.d11;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Point3D;

public class Day11 {
    FuelCellGrid grid;

    public Point3D getPart1() {
        return grid.getHighestValueFuelCellWithVariableWidth(3).getKey();
    }

    public Point3D getPart2() {
        return grid.getHighestValueFuelCellWithVariableWidth(300).getKey();
    }

    public void readInput(String path) {
        this.grid = new FuelCellGrid(Input.getSingleLine(path));
    }

    public void SetSerialNumber(int serialNumber) {
        grid = new FuelCellGrid(serialNumber);
    }

    public Day11() {
        System.out.println("::: Starting Day 11 :::");
        String inputPath = "y2018/d11/input.txt";
        readInput(inputPath);
        Point3D part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        Point3D part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
