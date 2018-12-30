package krabban91.kodvent.kodvent.utilities.search;

import java.util.Collection;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

public class Graph<P extends Path, E extends Edge> {

    public P search(PriorityQueue<P> unchecked, BiFunction<P, Map<String, Collection<E>>, Collection<P>> getNextFunction, Map<String, Collection<E>> map) {
        while (!unchecked.isEmpty()) {
            P poll = unchecked.poll();
            if (poll.isTarget()) {
                return poll;
            }
            unchecked.addAll(getNextFunction.apply(poll, map));
        }
        return null;
    }
}
