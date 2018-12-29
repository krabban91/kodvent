package krabban91.kodvent.kodvent.y2015.d07;

import krabban91.kodvent.kodvent.y2015.d07.expressions.Expr;
import krabban91.kodvent.kodvent.y2015.d07.expressions.Value;
import krabban91.kodvent.kodvent.y2015.d07.expressions.Variable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BobbyTablesKit {
    private static boolean debug = false;

    private final Map<String, Expr> operations;

    public BobbyTablesKit(List<Operation> operations) {
        this.operations = operations.stream()
                .collect(Collectors.toMap(
                        Operation::getResult,
                        Operation::getInput));
    }

    public static boolean isDebugging() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        BobbyTablesKit.debug = debug;
    }

    public char getValueOf(String a) {
        return getValueOf(a, cloneOperations());
    }

    public char getValueOf(String a, Map<String, Expr> ops) {
        Variable.setOperations(ops);
        return new Variable(a).eval();
    }

    public long complexRoutine() {
        char aVal = getValueOf("a");
        Map<String, Expr> ops = cloneOperations();
        ops.put("b", new Value(aVal));
        return getValueOf("a", ops);
    }

    public Map<String, Expr> cloneOperations() {
        return operations.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
