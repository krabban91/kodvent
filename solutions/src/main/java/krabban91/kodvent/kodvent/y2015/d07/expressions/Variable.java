package krabban91.kodvent.kodvent.y2015.d07.expressions;

import krabban91.kodvent.kodvent.y2015.d07.BobbyTablesKit;

import java.util.Map;

public class Variable implements Expr {

    private static Map<String, Expr> operations;
    private final String name;

    public Variable(String value) {
        this.name = value;
    }

    public static void setOperations(Map<String, Expr> ops) {
        Variable.operations = ops;
    }

    @Override
    public char eval() {
        Expr g = operations.get(name);
        if (g instanceof Gate) {
            String expanded = g.show(10);
            char result = g.eval();
            if(BobbyTablesKit.isDebugging()){
                System.out.println(name+"="+(int)result+", expanded: "+expanded);
            }
            operations.put(name, new Value(result));

        }
        return operations.get(name).eval();
    }

    @Override
    public String show(int depth) {
        if (depth > 0) {
            return name+"(" + operations.get(name).show(depth - 1) + ")";
        }
        return name+"(...)";
    }
}
