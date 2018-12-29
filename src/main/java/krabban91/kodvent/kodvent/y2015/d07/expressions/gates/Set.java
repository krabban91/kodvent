package krabban91.kodvent.kodvent.y2015.d07.expressions.gates;

import krabban91.kodvent.kodvent.y2015.d07.expressions.Expr;
import krabban91.kodvent.kodvent.y2015.d07.expressions.Gate;

public class Set extends Gate {

    String format = "%s %s";
    String op = "SET";
    Expr left;

    public Set(String s) {
        left = getExpr(s);
    }

    @Override
    public char eval() {
        return left.eval();
    }

    @Override
    public String show(int depth) {
        return String.format(format, op, left.show(depth));
    }
}
