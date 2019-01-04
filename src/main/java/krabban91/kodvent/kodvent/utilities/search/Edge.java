package krabban91.kodvent.kodvent.utilities.search;

import java.util.Set;

public interface Edge<V> {

    Set<V> vertices();

    V leadsTo(V from);

    boolean isConnectedTo(V vertex);

    int cost();
}
