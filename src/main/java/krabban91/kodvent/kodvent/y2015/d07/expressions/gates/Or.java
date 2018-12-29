package krabban91.kodvent.kodvent.y2015.d07.expressions.gates;

import krabban91.kodvent.kodvent.y2015.d07.expressions.Expr;
import krabban91.kodvent.kodvent.y2015.d07.expressions.Gate;

public class Or extends Gate {

    String format = "%s %s %s";
    String op = "OR";
    Expr left;
    Expr right;

    public Or(String s) {
        String[] split1 = s.split(" OR ");
        left = getExpr(split1[0]);
        right = getExpr(split1[1]);
    }

    @Override
    public char eval() {
        return (char) (left.eval() | right.eval());
    }

    @Override
    public String show(int depth) {
        return String.format(format, left.show(depth), op, right.show(depth));
    }
}

