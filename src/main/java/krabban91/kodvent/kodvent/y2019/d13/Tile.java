package krabban91.kodvent.kodvent.y2019.d13;

public class Tile {
    private long type;

    public Tile(Long l) {
        this.type = l;
    }

    public boolean isBlock(){
        return type == 2;
    }
}
