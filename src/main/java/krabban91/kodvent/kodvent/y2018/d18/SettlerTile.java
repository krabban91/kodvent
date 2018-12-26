package krabban91.kodvent.kodvent.y2018.d18;

public class SettlerTile {


    Type oldState;
    Type newState;

    public enum Type {
        OPEN,
        FOREST,
        LUMBERMILL
    }

    public SettlerTile(Type oldState, Type newState){
        this.oldState = oldState;
        this.newState = newState;
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

    public boolean isOpen(){
        return this.oldState == Type.OPEN;
    }


    public boolean isForest(){
        return this.oldState == Type.FOREST;
    }

    public boolean isLumberMill(){
        return this.oldState == Type.LUMBERMILL;
    }

    @Override
    public SettlerTile clone(){
        return new SettlerTile(oldState, newState);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SettlerTile){
            SettlerTile other = (SettlerTile)obj;
            return this.oldState == other.oldState;
        }
        return false;
    }
}
