package krabban91.kodvent.kodvent.day11;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day11 {
    private static String inputPath = "day11.txt";
    FuelCellGrid grid;

    public Point3D getPart1() {
        return grid.getHighestValueFuelCellWithVariableWidth(3).getKey();
    }

    public Point3D getPart2() {
        return grid.getHighestValueFuelCellWithVariableWidth(300).getKey();
    }


    public FuelCellGrid readInput(Stream<String> stream) {
        return stream
                .map(FuelCellGrid::new)
                .findFirst().get();
    }

    public void SetSerialNumber(int serialNumber) {
        grid = new FuelCellGrid(serialNumber);
    }

    public Day11() {
        System.out.println("::: Starting Day 11 :::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            grid = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Point3D part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        Point3D part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
