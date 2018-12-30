package krabban91.kodvent.kodvent.y2015.d09;

public class Route {

    private String current;
    private Route pathToHere;
    private int costToHere;

    public Route(String start) {
        current = start;
        costToHere = 0;
    }

    public Route(Route previous, String next, int cost) {
        current = next;
        pathToHere = previous;
        costToHere = cost;
    }

    public int totalDistance() {
        return costToHere + (pathToHere != null ? pathToHere.totalDistance() : 0);
    }

    public int reachedCities() {
        return 1 + (pathToHere != null ? pathToHere.reachedCities() : 0);
    }

    public String getCity() {
        return current;
    }

    public boolean containsCity(String destination) {
        return destination.equals(current) || (pathToHere != null && pathToHere.containsCity(destination));
    }
}
