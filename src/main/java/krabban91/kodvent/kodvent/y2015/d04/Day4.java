package krabban91.kodvent.kodvent.y2015.d04;

import krabban91.kodvent.kodvent.utilities.Input;
import krabban91.kodvent.kodvent.utilities.Strings;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

public class Day4 {
    private String secret;

    public int getPart1() {
        return findKeyForChecksum(5);
    }

    public int getPart2() {
        return findKeyForChecksum(6);
    }

    private int findKeyForChecksum(int times) {
        int result = 1;
        String target = Strings.repeated("0", times);
        while (true) {
            try {
                if (checkSumStartsWith(result, target)) {
                    return result;
                }
                result++;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkSumStartsWith(int key, String target) throws UnsupportedEncodingException {
        return DigestUtils
                .md5DigestAsHex((secret + key).getBytes("UTF-8"))
                .substring(0, target.length())
                .equals(target);
    }

    public void readInput(String inputPath) {
        secret = Input.getSingleLine(inputPath);
    }

    public Day4() {
        System.out.println("::: Starting Day 4 :::");
        String inputPath = "y2015/d04/input.txt";
        readInput(inputPath);
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
