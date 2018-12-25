package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NanoGrid {

    List<NanoBot> nanoBots;
    Point3D startLocation = new Point3D(0, 0, 0);

    public NanoGrid(Stream<String> input) {
        this.nanoBots = input.map(NanoBot::new).collect(Collectors.toList());
    }

    public long numberOfNanobotsInRangeOfStrongest() {
        Optional<NanoBot> max = nanoBots.stream().parallel().max(Comparator.comparingInt(b -> b.getSignalRadius()));
        return max.map(nanoBot -> nanoBots.stream().filter(b -> b.manhattandistanceTo(nanoBot) <= nanoBot.getSignalRadius()).count()).orElse(0L);
    }

    public long distanceToBestLocationToStand() {
        //top5% reached byOthers;
        int minX = getLimitValue(nanoBots.stream().parallel().map(NanoBot::getX).min(Comparator.comparingInt(b -> b)).orElse(1));

        int minY = getLimitValue(nanoBots.stream().parallel().map(NanoBot::getY).min(Comparator.comparingInt(b -> b)).orElse(1));
        int minZ = getLimitValue(nanoBots.stream().parallel().map(NanoBot::getZ).min(Comparator.comparingInt(b -> b)).orElse(1));
        Point3D minPoint = new Point3D(minX, minY, minZ);
        int maxX = getLimitValue(nanoBots.stream().parallel().map(NanoBot::getX).max(Comparator.comparingInt(b -> b)).orElse(1));
        int maxY = getLimitValue(nanoBots.stream().parallel().map(NanoBot::getY).max(Comparator.comparingInt(b -> b)).orElse(1));
        int maxZ = getLimitValue(nanoBots.stream().parallel().map(NanoBot::getZ).max(Comparator.comparingInt(b -> b)).orElse(1));
        Point3D maxPoint = new Point3D(maxX, maxY, maxZ);
        AtomicInteger currentScale = new AtomicInteger((maxX - minX));
        Stream<SearchGrid> searchGrid = new SearchGrid(minPoint, maxPoint, currentScale.get(), 2).createGridsWithLowerScale(2);
        currentScale.set(currentScale.get()/2);
        Set<SearchGrid> afterSearch;
        while (currentScale.get()> 1){
            afterSearch = searchGrid
                    .collect(maxSet(Comparator.comparingLong(g -> g.botsWithinRange(nanoBots))));
            currentScale.set(afterSearch.stream().findAny().get().getScale());
            System.out.println("############");
            System.out.println("current Scale: "+currentScale.get());
            System.out.println("search set size after  : "+afterSearch.size());
            searchGrid = afterSearch.parallelStream()
                    .map(s -> s.createGridsWithLowerScale(2))
                    .reduce(Stream::concat).orElseGet(Stream::empty);
        }
        afterSearch = searchGrid
                .collect(maxSet(Comparator.comparingLong(g -> g.botsWithinRange(nanoBots))));
        Set<StandingLocation> informedLocations = afterSearch.parallelStream()
                .map(SearchGrid::getPointsInGrid)
                .flatMap(Set::stream)
                .map(p -> new StandingLocation(p, nanoBots.stream()
                        .filter(b -> b.manhattandistanceTo(p) <= b.getSignalRadius())
                        .collect(Collectors.toSet())))
                .collect(Collectors.toSet());

        long maxBotCount = informedLocations.stream().max(Comparator.comparingLong(l -> l.getNanoBotsWithinReach())).get().nanoBotsWithinReach;

        // 156889430 is too low.
        return informedLocations.stream()
                .filter(l -> l.getNanoBotsWithinReach() >= maxBotCount)
                .map(l -> l.manhattandistanceTo(startLocation))
                .min(Comparator.comparingInt(d -> d)).orElse(0);
    }

    private int getLimitValue(int input) {
        if (input == 0) {
            return -2;
        }
        int limit = input / Math.abs(input);
        while (input / limit > 0) {
            limit *= 2;
        }
        return input > 0 ? limit/2 : limit;
    }

    static <T> Collector<T,?,Set<T>> maxSet(Comparator<? super T> comp) {
        return Collector.of(
                HashSet::new,
                (set, t) -> {
                    int c;
                    if (set.isEmpty() || (c = comp.compare(t, set.stream().findAny().get())) == 0) {
                        set.add(t);
                    } else if (c > 0) {
                        set.clear();
                        set.add(t);
                    }
                },
                (set1, set2) -> {
                    if (set1.isEmpty()) {
                        return set2;
                    }
                    if (set2.isEmpty()) {
                        return set1;
                    }
                    int r = comp.compare(set1.stream().findAny().get(), set2.stream().findAny().get());
                    if (r < 0) {
                        return set2;
                    } else if (r > 0) {
                        return set1;
                    } else {
                        set1.addAll(set2);
                        return set1;
                    }
                });
    }
}
