package krabban91.kodvent.kodvent.day18;

public class SettlerTile {


    Type oldState;
    Type newState;

    public enum Type {
        OPEN,
        FOREST,
        LUMBERMILL
    }

    public SettlerTile(int charValue){
        if(charValue == (int)'#'){
            this.oldState = Type.LUMBERMILL;
        }else if(charValue == (int)'|'){
            this.oldState = Type.FOREST;
        } else if(charValue == (int)'.'){
            this.oldState = Type.OPEN;
        }
        this.newState = this.oldState;
    }

    @Override
    public String toString() {
        if(oldState == Type.LUMBERMILL){
            return "#";
        }
        if(oldState == Type.FOREST){
            return "|";
        }
        return ".";
    }

    public static void oldToNew(SettlerTile tile){
        tile.oldState = tile.newState;
    }
}
