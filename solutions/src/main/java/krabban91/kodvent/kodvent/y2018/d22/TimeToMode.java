package krabban91.kodvent.kodvent.y2018.d22;

import krabban91.kodvent.kodvent.utilities.Distances;
import krabban91.kodvent.kodvent.utilities.search.Path;

public class TimeToMode implements Path<Mode> {

    private Mode current;
    private TimeToMode previous;
    private int cost;
    private Mode target;

    public TimeToMode(Mode start, Mode target) {
        this.current = start;
        this.cost = 0;
        this.target = target;
    }

    public TimeToMode(TimeToMode previous, Transition action) {
        this.previous = previous;
        this.current = action.leadsTo(previous.destination());
        this.cost = action.cost();
        this.target = previous.target;
    }

    public int getHeuristic(){
        return cost() + Distances.manhattan(this.destination().getRegion().getPoint(), target.getRegion().getPoint());
    }

    @Override
    public Mode destination() {
        return current;
    }

    @Override
    public boolean hasVisited(Mode destination) {
        return this.current.equals(destination) || (this.previous != null && this.previous.hasVisited(destination));
    }

    @Override
    public boolean isTarget() {
        return this.current.equals(this.target);
    }

    @Override
    public int cost() {
        return cost + (this.previous != null ? this.previous.cost() : 0);
    }

    public TimeToMode getPrevious() {
        return previous;
    }
}
