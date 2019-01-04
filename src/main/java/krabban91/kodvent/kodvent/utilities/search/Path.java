package krabban91.kodvent.kodvent.utilities.search;

public interface Path<V> {

    V destination();

    boolean hasVisited(V destination);

    boolean isTarget();

    int cost();
}
