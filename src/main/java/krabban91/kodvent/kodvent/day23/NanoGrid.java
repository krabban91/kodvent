package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NanoGrid {

    List<NanoBot> nanoBots;
    Point3D startLocation = new Point3D(0, 0, 0);

    public NanoGrid(Stream<String> input) {
        this.nanoBots = input.map(NanoBot::new).collect(Collectors.toList());
    }

    public long numberOfNanobotsInRangeOfStrongest() {
        Optional<NanoBot> max = nanoBots.stream().max(Comparator.comparingInt(b -> b.getSignalRadius()));
        return max.map(nanoBot -> nanoBots.stream().filter(b -> b.manhattandistanceTo(nanoBot) <= nanoBot.getSignalRadius()).count()).orElse(0L);
    }

    public long distanceToBestLocationToStand() {
        //top5% reached byOthers;
        int minX = getLimitValue(nanoBots.stream().map(NanoBot::getX).min(Comparator.comparingInt(b -> b)).orElse(1));

        int minY = getLimitValue(nanoBots.stream().map(NanoBot::getY).min(Comparator.comparingInt(b -> b)).orElse(1));
        int minZ = getLimitValue(nanoBots.stream().map(NanoBot::getZ).min(Comparator.comparingInt(b -> b)).orElse(1));
        Point3D minPoint = new Point3D(minX, minY, minZ);
        int maxX = getLimitValue(nanoBots.stream().map(NanoBot::getX).max(Comparator.comparingInt(b -> b)).orElse(1));
        int maxY = getLimitValue(nanoBots.stream().map(NanoBot::getY).max(Comparator.comparingInt(b -> b)).orElse(1));
        int maxZ = getLimitValue(nanoBots.stream().map(NanoBot::getZ).max(Comparator.comparingInt(b -> b)).orElse(1));
        Point3D maxPoint = new Point3D(maxX, maxY, maxZ);
        AtomicInteger currentScale = new AtomicInteger((maxX - minX));
        Set<SearchGrid> searchGrid = new SearchGrid(minPoint, maxPoint, currentScale.get()).createGridsWithLowerScale(2);
        currentScale.set(currentScale.get()/2);
        AtomicLong l1 = new AtomicLong(0);
        Set<SearchGrid> afterSearch;
        while (currentScale.get()> 1){
            l1.set(searchGrid.stream().map(grid -> grid.botsWithinRange(nanoBots)).max(Comparator.comparingLong(l -> l)).orElse(0L));
            afterSearch = searchGrid.stream()
                    .filter(s -> s.botsWithinRange(nanoBots) >= l1.get())
                    .collect(Collectors.toSet());
            System.out.println("############");
            System.out.println("current Scale: "+currentScale.get());
            System.out.println("robots withinRange: "+l1.get());
            System.out.println("searchGrid.size : "+searchGrid.size());
            System.out.println("afterSearch.size : "+afterSearch.size());
            searchGrid = afterSearch.stream()
                    .map(s -> s.createGridsWithLowerScale(2))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
            currentScale.set(searchGrid.stream().findAny().get().getScale());
        }
        l1.set(searchGrid.stream().map(grid -> grid.botsWithinRange(nanoBots)).max(Comparator.comparingLong(l -> l)).orElse(0L));
        afterSearch = searchGrid.stream()
                .filter(s -> s.botsWithinRange(nanoBots) >= l1.get())
                .collect(Collectors.toSet());
        Set<StandingLocation> informedLocations = afterSearch.stream()
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
}
