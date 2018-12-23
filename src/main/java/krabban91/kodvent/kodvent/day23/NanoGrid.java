package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NanoGrid {

    List<NanoBot> nanoBots;
    Point3D startLocation = new Point3D(0,0,0);

    public NanoGrid(Stream<String> input) {
        this.nanoBots = input.map(NanoBot::new).collect(Collectors.toList());
    }

    public long numberOfNanobotsInRangeOfStrongest() {
        Optional<NanoBot> max = nanoBots.stream().max(Comparator.comparingInt(b -> b.getSignalRadius()));
        return max.map(nanoBot -> nanoBots.stream().filter(b -> b.manhattandistanceTo(nanoBot) <= nanoBot.getSignalRadius()).count()).orElse(0L);
    }

    public long distanceToBestLocationToStand() {
        Set<Point3D> plausibleLocations = nanoBots.stream()
                .map(NanoBot::getLocationsWithinReach)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        Set<StandingLocation> informedLocations = plausibleLocations.stream()
                .map(p -> new StandingLocation(p, nanoBots.stream()
                        .filter(n -> n.manhattandistanceTo(p) <= n.getSignalRadius())
                        .count())).collect(Collectors.toSet());
        long maxBotCount = informedLocations.stream().max(Comparator.comparingLong(l -> l.getNanoBotsWithinReach())).get().nanoBotsWithinReach;

        return informedLocations.stream()
                .filter(l -> l.getNanoBotsWithinReach() >=maxBotCount)
                .map(l -> l.manhattandistanceTo(startLocation))
                .min(Comparator.comparingInt(d -> d)).orElse(0);
    }

}
