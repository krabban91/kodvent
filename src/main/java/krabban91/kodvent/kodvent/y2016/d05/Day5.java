package krabban91.kodvent.kodvent.y2016.d05;

import krabban91.kodvent.kodvent.utilities.Input;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class Day5 {

    String in;

    public Day5() {
        System.out.println("::: Starting Day 4 :::");
        String inputPath = "y2016/d04/input.txt";
        readInput(inputPath);
        String part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        String part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public String getPart1() {
        return getPassword(in);
    }

    public String getPart2() {
        return getPasswordHarder(in);
    }

    private String getPassword(String in) {
        int size = 8;
        StringBuilder b = new StringBuilder();
        int suffix = 0;
        for (int i = 0; i < size; i++) {
            String hash = DigestUtils.md5Hex(in + suffix);
            while (!(hash.indexOf("00000") == 0)) {
                suffix++;
                hash = DigestUtils.md5Hex(in + suffix);
            }
            b.append(hash.charAt(5));
            suffix++;
        }
        return b.toString();
    }

    private String getPasswordHarder(String in) {
        Character[] password = new Character[8];
        int suffix = 0;
        while (notComplete(password)) {
            String hash = DigestUtils.md5Hex(in + suffix);
            while (!(hash.indexOf("00000") == 0)) {
                suffix++;
                hash = DigestUtils.md5Hex(in + suffix);
            }
            char loc = hash.charAt(5);
            if (loc <= '7' && loc >= '0' && password[Integer.parseInt(loc + "")] == null) {
                password[Integer.parseInt(loc + "")] = hash.charAt(6);
            }
            suffix++;
        }
        StringBuilder b = new StringBuilder();
        for (Character character : password) {
            b.append(character);
        }
        return b.toString();
    }

    private boolean notComplete(Character[] password) {
        for (Character character : password) {
            if (character == null) {
                return true;
            }
        }
        return false;
    }

    public void readInput(String inputPath) {
        in = Input.getSingleLine(inputPath);
    }
}
