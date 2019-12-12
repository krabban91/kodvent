package krabban91.kodvent.kodvent.utilities;

public class MathUtils {

    public static long LCM(long a, long b) {
        return (a * b) / GCD(a, b);
    }

    public static long GCD(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return (GCD(b, a % b));
        }
    }
}
