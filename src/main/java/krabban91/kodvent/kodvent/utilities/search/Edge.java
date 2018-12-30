package krabban91.kodvent.kodvent.utilities.search;

import java.util.Set;

public interface Edge {

    Set<String> vertices();

    String leadsTo(String from);

    boolean isConnectedTo(String vertex);

    int cost();
}
