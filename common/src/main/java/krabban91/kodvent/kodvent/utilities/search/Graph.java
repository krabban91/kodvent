package krabban91.kodvent.kodvent.utilities.search;

import java.util.Collection;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Graph<P extends Path, E extends Edge, V> {

    public P search(PriorityQueue<P> unchecked, BiFunction<P, Map<V, Collection<E>>, Collection<P>> getNextFunction, Map<V, Collection<E>> map) {
        while (!unchecked.isEmpty()) {
            P poll = unchecked.poll();
            if (poll.isTarget()) {
                return poll;
            }
            unchecked.addAll(getNextFunction.apply(poll, map));
        }
        return null;
    }

    public P search(PriorityQueue<P> unchecked, BiFunction<P, Map<V, Collection<E>>, Collection<P>> getNextFunction, Map<V, Collection<E>> map, Function<P, Boolean> earlyStopping) {
        while (!unchecked.isEmpty()) {
            P poll = unchecked.poll();
            if (poll.isTarget()) {
                return poll;
            }
            if(earlyStopping.apply(poll)){
                return poll;
            }
            unchecked.addAll(getNextFunction.apply(poll, map));
        }
        return null;
    }
}
