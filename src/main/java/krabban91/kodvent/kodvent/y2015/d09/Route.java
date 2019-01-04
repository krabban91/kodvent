package krabban91.kodvent.kodvent.y2015.d09;

import krabban91.kodvent.kodvent.utilities.search.Path;

public class Route implements Path<String> {

    private String current;
    private Route pathToHere;
    private int costToHere;
    private int targetSize;

    public Route(String start, int targetSize) {
        current = start;
        costToHere = 0;
        this.targetSize = targetSize;
    }

    public Route(Route previous, Road road, int targetSize) {
        current = road.leadsTo(previous.current);
        pathToHere = previous;
        costToHere = road.cost();
        this.targetSize = targetSize;
    }


    @Override
    public boolean hasVisited(String destination) {
        return destination.equals(current) || (pathToHere != null && pathToHere.hasVisited(destination));
    }

    @Override
    public String destination() {
        return current;
    }

    @Override
    public boolean isTarget() {
        return this.reachedCities() == this.targetSize;
    }

    @Override
    public int cost() {
        return costToHere + (pathToHere != null ? pathToHere.cost() : 0);
    }

    private int reachedCities() {
        return 1 + (pathToHere != null ? pathToHere.reachedCities() : 0);
    }

    public int getTargetSize() {
        return targetSize;
    }
}
