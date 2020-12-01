package krabban91.kodvent.kodvent.y2015.d07.expressions.gates;

import krabban91.kodvent.kodvent.y2015.d07.expressions.Expr;
import krabban91.kodvent.kodvent.y2015.d07.expressions.Gate;

public class Not extends Gate {

    String format = "%s %s";
    String op = "NOT";
    Expr left;

    public Not(String s) {
        String[] split1 = s.split("NOT ");
        left = getExpr(split1[1]);
    }

    @Override
    public char eval() {

        return (char) (~left.eval());
    }

    @Override
    public String show(int depth) {
        return String.format(format, op, left.show(depth));
    }
}
