package krabban91.kodvent.kodvent.y2019.d13;

public class Tile {
    private int type;

    public Tile(Long l) {
        this.type = l.intValue();
    }

    public boolean isBlock() {
        return type == 2;
    }

    @Override
    public String toString() {
        switch (type) {
            case 0:
                return " ";
            case 1:
                return "|";
            case 2:
                return "x";
            case 3:
                return "_";
            case 4:
                return "O";
        }
        return "";
    }

    public boolean isBall() {
        return type == 4;
    }

    public boolean isPaddle() {
        return type== 3;
    }

    public boolean isWall() {
        return type == 1;
    }
}
