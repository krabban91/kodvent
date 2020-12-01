package krabban91.kodvent.kodvent.y2015.d07.expressions.gates;

import krabban91.kodvent.kodvent.y2015.d07.expressions.Expr;
import krabban91.kodvent.kodvent.y2015.d07.expressions.Gate;

import java.util.Map;

public class And extends Gate {

    String format = "%s %s %s";
    String op = "AND";
    Expr left;
    Expr right;

    public And(String s) {
        String[] split1 = s.split(" AND ");
        left = getExpr(split1[0]);
        right = getExpr(split1[1]);
    }

    @Override
    public char eval() {
        return (char)(left.eval() & right.eval());
    }

    @Override
    public String show(int depth) {
        return String.format(format, left.show(depth), op, right.show(depth));
    }
}
