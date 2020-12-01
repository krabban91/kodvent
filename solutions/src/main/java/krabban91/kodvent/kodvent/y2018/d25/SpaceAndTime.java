package krabban91.kodvent.kodvent.y2018.d25;

import krabban91.kodvent.kodvent.utilities.TimePoint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SpaceAndTime {

    private Set<TimePoint> points;

    public SpaceAndTime(List<String> input) {
        points = input.stream().map(TimePoint::new).collect(Collectors.toSet());
    }

    public int countConstellations() {
        Set<Set<TimePoint>> constellations = new HashSet<>();
        points.forEach(p -> {
            Set<Set<TimePoint>> collect = constellations.stream()
                    .filter(c -> c.stream()
                            .anyMatch(o -> o.manhattanDistance(p) <= 3))
                    .collect(Collectors.toSet());
            if (collect.isEmpty()) {
                // new constellation
                Set<TimePoint> newConstellation = new HashSet<>();
                newConstellation.add(p);
                constellations.add(newConstellation);
            } else {
                if (collect.size() == 1) {
                    // add to constellation
                    constellations.removeAll(collect);
                    collect.forEach(c -> c.add(p));
                    constellations.add(collect.stream().findAny().get());
                } else {
                    // merging constellations
                    constellations.removeAll(collect);
                    Set<TimePoint> merged = collect.stream().flatMap(Set::stream).collect(Collectors.toSet());
                    merged.add(p);
                    constellations.add(merged);
                }
            }
        });
        return constellations.size();
    }
}
