package krabban91.kodvent.kodvent.y2019.d22;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DealingAction {
    static final int NEW_STACK = 0;
    static final int CUT = 1;
    static final int INCREMENT = 2;


    int type;

    int parameter;

    public DealingAction(String s) {
        if (s.equals("deal into new stack")) {
            type = NEW_STACK;
            parameter = 0;
        } else {

            String[] split = s.split(" ");
            if (split[0].equals("cut")) {
                type = CUT;
                parameter = Integer.parseInt(split[1]);
            }
            if (split[0].equals("deal")) {
                type = INCREMENT;
                parameter = Integer.parseInt(split[3]);
            }
        }

    }

    public List<Long> handle(List<Long> deck) {
        switch (type) {
            case NEW_STACK:
                ArrayList<Long> cards = new ArrayList<>(deck);
                Collections.reverse(cards);
                return cards;
            case CUT:
                ArrayList<Long> cutOut = new ArrayList<>();
                if (parameter > 0) {
                    cutOut.addAll(deck.subList(parameter, deck.size()));
                    cutOut.addAll(deck.subList(0, parameter));
                    return cutOut;
                } else {
                    cutOut.addAll(deck.subList(deck.size() + parameter, deck.size()));
                    cutOut.addAll(deck.subList(0, deck.size() + parameter));
                    return cutOut;
                }
            case INCREMENT:
                Map<Integer, Long> incrementOut = new HashMap<>();
                for (int i = 0; i < deck.size(); i++) {
                    incrementOut.put((i * parameter) % deck.size(), deck.get(i));
                }
                return new ArrayList<>(incrementOut.values());
        }
        return deck;
    }

    // polynomial (a*x + b)^n % L
    public Map.Entry<BigInteger, BigInteger> adjustInversePolynomialParameters(Map.Entry<BigInteger, BigInteger> parameters, BigInteger size){
        BigInteger a = parameters.getKey();
        BigInteger b = parameters.getValue();
        switch (type) {
            case NEW_STACK:
                // -ax + (size - b - 1) % size
                return Map.entry(a.negate(),size.subtract(b).subtract(BigInteger.ONE));
            case CUT:
                // ax + b + param % size
                return Map.entry(a, b.add(BigInteger.valueOf(parameter)).mod(size));
            default: // INCREMENT
                // (a*modinv(param, size))x + b*modinv(param, size) % size
                BigInteger z = BigInteger.valueOf(parameter).modInverse(size);
                return Map.entry(a.multiply(z).mod(size),b.multiply(z).mod(size));
        }
    }

    // polynomial (a*x + b)^n % L
    public Map.Entry<BigInteger, BigInteger> adjustPolynomialParameters(Map.Entry<BigInteger, BigInteger> parameters, BigInteger size){
        BigInteger a = parameters.getKey();
        BigInteger b = parameters.getValue();
        switch (type) {
            case NEW_STACK:
                // -ax + (size - b - 1) % size
                return Map.entry(a.negate(),size.subtract(b).subtract(BigInteger.ONE));
            case CUT:
                // ax + (b + size - param) % size
                return Map.entry(a, b.add(size).subtract(BigInteger.valueOf(parameter)).mod(size));
            default: // INCREMENT
                // (a*param)x + b * param % size
                BigInteger z = BigInteger.valueOf(parameter);
                return Map.entry(a.multiply(z).mod(size),b.multiply(z).mod(size));
        }
    }

    /*
    # modpow the polynomial: (ax+b)^m % n
    # f(x) = ax+b
    # g(x) = cx+d
    # f^2(x) = a(ax+b)+b = aax + ab+b
    # f(g(x)) = a(cx+d)+b = acx + ad+b
     */
    public static Map.Entry<BigInteger, BigInteger> polypow(BigInteger a, BigInteger b, BigInteger m, BigInteger n) {
        if (m.equals(BigInteger.ZERO)) {
            return Map.entry(BigInteger.ONE, BigInteger.ZERO);
        }
        if (m.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return polypow(a.multiply(a).mod(n), a.multiply(b).add(b).mod(n), m.divide(BigInteger.valueOf(2)), n);
        }
        Map.Entry<BigInteger, BigInteger> polypow = polypow(a, b, m.subtract(BigInteger.valueOf(1)), n);
        BigInteger c = polypow.getKey(), d = polypow.getValue();
        return Map.entry(a.multiply(c).mod(n), (a.multiply(d).add(b).mod(n)));
    }

    public static List<Long> shuffle(List<Long> deck, List<DealingAction> actions) {
        ArrayList<DealingAction> dealingActions = new ArrayList<>(actions);
        for (DealingAction action : dealingActions) {
            deck = action.handle(deck);
        }
        return deck;
    }
}
