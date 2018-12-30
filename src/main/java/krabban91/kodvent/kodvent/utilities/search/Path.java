package krabban91.kodvent.kodvent.utilities.search;

public interface Path {

    String destination();

    boolean hasVisited(String destination);

    boolean isTarget();

    int cost();
}
