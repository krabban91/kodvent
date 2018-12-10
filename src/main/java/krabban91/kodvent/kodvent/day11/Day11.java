package krabban91.kodvent.kodvent.day11;

import javafx.geometry.Point3D;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class Day11 {
    private static String inputPath = "day11.txt";
    FuelCellGrid grid;

    public Point getPart1() {
        return grid.gethighestValueFuelCell().getKey();
    }

    public Point3D getPart2() {
        return grid.gethighestValueFuelCellWithVariableWith().getKey();
    }


    public FuelCellGrid readInput(Stream<String> stream) {
        return stream
                .map(FuelCellGrid::new)
                .findFirst().get();
    }

    public void SetSerialNumber(int serialNumber){
        grid = new FuelCellGrid(serialNumber);
    }

    public Day11() {
        System.out.println("::: Starting Day 11 :::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            grid = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Point part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1.x+","+part1.y);
        Point3D part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2.getX()+","+part2.getY()+""+part2.getZ());
    }
}
