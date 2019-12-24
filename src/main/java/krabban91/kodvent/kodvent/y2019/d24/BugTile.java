package krabban91.kodvent.kodvent.y2019.d24;

import krabban91.kodvent.kodvent.utilities.logging.Loggable;

import java.util.Collection;
import java.util.Objects;

public class BugTile implements Loggable {

    boolean bug;

    public BugTile(boolean isBug) {
        bug = isBug;
    }

    public boolean isBug() {
        return bug;
    }

    public BugTile nextState(Collection<BugTile> adjacent){
        long count = adjacent.stream().filter(BugTile::isBug).count();
        if(this.isBug()){
            return new BugTile(count == 1L);
        } else {
            return new BugTile(count ==1 || count==2);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BugTile bugTile = (BugTile) o;
        return bug == bugTile.bug;
    }


    @Override
    public int hashCode() {
        return Objects.hash(bug);
    }

    @Override
    public String showTile() {
        return isBug()?"#":".";
    }
}
