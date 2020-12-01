package krabban91.kodvent.kodvent.y2018.d20;

import krabban91.kodvent.kodvent.utilities.search.Path;

public class DoorsToRoom implements Path<Room> {
    private DoorsToRoom previous;
    Room current;
    int distance;

    public DoorsToRoom(DoorsToRoom previous, Room current){
        this.current = current;
        this.previous = previous;
        this.distance = 1;
    }

    public DoorsToRoom(Room start){
        this.current = start;
        this.distance = 0;
    }

    @Override
    public int cost() {
        return this.distance + (this.previous != null ? this.previous.cost() : 0);
    }

    @Override
    public Room destination() {
        return current;
    }

    @Override
    public boolean hasVisited(Room destination) {
        return this.destination().getPoint().equals(destination.getPoint()) || (this.previous != null && this.previous.hasVisited(destination));
    }

    @Override
    public boolean isTarget() {
        return false;
    }
}
