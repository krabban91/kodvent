package krabban91.kodvent.kodvent.y2018.d08;

import krabban91.kodvent.kodvent.utilities.Input;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 {
    LicenseTreeNode licenseTree;

    public int getPart1() {
        return licenseTree.getCheckSum();
    }

    public int getPart2() {
        return licenseTree.getMetaDataSum();
    }

    public void readInput(String path) {
        this.licenseTree = parseLicense(Input.getSingleLine(path));
    }

    public LicenseTreeNode parseLicense(String row) {
        List<String> strings = Arrays.asList(row.split(" "));
        return new LicenseTreeNode(
                new InputContainer(strings
                        .stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())));
    }

    public Day8() {
        System.out.println("::: Starting Day 8 :::");
        String inputPath = "y2018/d08/input.txt";
        readInput(inputPath);
        int part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public LicenseTreeNode getLicenseTree() {
        return licenseTree;
    }
}
