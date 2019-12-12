package krabban91.kodvent.kodvent.utilities;

public class MathUtils {

    public static long LCM(long a, long b) {
        return (a * b) / GCF(a, b);
    }

    public static long GCF(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return (GCF(b, a % b));
        }
    }
}
