package krabban91.kodvent.kodvent.y2015.d07.expressions;

public abstract class Gate implements Expr {

    public Expr getExpr(String s) {
        Expr expr;
        try {
            expr = new Value((char)Integer.parseInt(s));
        } catch (NumberFormatException e) {
            expr = new Variable(s);
        }
        return expr;
    }
}
