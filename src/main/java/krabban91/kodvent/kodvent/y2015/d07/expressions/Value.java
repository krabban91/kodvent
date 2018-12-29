package krabban91.kodvent.kodvent.y2015.d07.expressions;

public class Value implements Expr {

    private final char value;

    public Value(char value) {
        this.value = value;
    }

    @Override
    public char eval() {
        return value;
    }

    @Override
    public String show(int depth) {
        return "(" + (int) value + ")";
    }
}
