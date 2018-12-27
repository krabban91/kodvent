package krabban91.kodvent.kodvent.y2018.d23;

import krabban91.kodvent.kodvent.utilities.Point3D;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NanoGrid {

    public static final double STEP_SIZE_DELTA = 0.9999;
    List<NanoBot> nanoBots;
    Point3D startLocation = new Point3D(0, 0, 0);

    public NanoGrid(List<String> input) {
        this.nanoBots = input.stream().map(NanoBot::new).collect(Collectors.toList());
    }

    public long numberOfNanobotsInRangeOfStrongest() {
        Optional<NanoBot> max = nanoBots.stream().parallel().max(Comparator.comparingInt(b -> b.getSignalRadius()));
        return max.map(nanoBot -> nanoBots.stream().filter(b -> b.manhattandistanceTo(nanoBot) <= nanoBot.getSignalRadius()).count()).orElse(0L);
    }

    public long distanceToBestLocationToStand() {
        AtomicInteger stepSize = new AtomicInteger(1000 * 1000 * 100);
        StandingLocation bestPosition = new StandingLocation(startLocation);
        StandingLocation currentPosition = new StandingLocation(startLocation);
        AtomicLong botsInRange = new AtomicLong(botsInRange(currentPosition.getPoint()));
        AtomicBoolean selectedNewPoint = new AtomicBoolean(true);
        while (stepSize.get() > 1 || selectedNewPoint.get()) {
            selectedNewPoint.set(false);
            IntStream.of(-1, 0, 1).forEach(z ->
                    IntStream.of(-1, 0, 1).forEach(y ->
                            IntStream.of(-1, 0, 1).forEach(x -> {
                                Point3D position = new Point3D(
                                        currentPosition.point.getX() + x * stepSize.get(),
                                        currentPosition.point.getY() + y * stepSize.get(),
                                        currentPosition.point.getZ() + z * stepSize.get());
                                long count = botsInRange(position);
                                int distanceFromStart = bestPosition.manhattandistanceTo(startLocation);
                                if (count > botsInRange.get()) {
                                    botsInRange.set(count);
                                    bestPosition.setPoint(position);
                                    selectedNewPoint.set(true);
                                } else if (count == botsInRange.get() && distanceFromStart < bestPosition.getDistance()) {
                                    bestPosition.setPoint(position);
                                    selectedNewPoint.set(true);
                                }
                            })));
            if (bestPosition.equals(currentPosition)) {
                stepSize.updateAndGet(i -> (int) (i * STEP_SIZE_DELTA));
            } else {
                currentPosition.setPoint(bestPosition.getPoint());
            }
            System.out.println("Position: " + currentPosition.getPoint());
            System.out.println("Distance: " + currentPosition.getDistance());
            System.out.println("StepSize: " + stepSize.get());
            System.out.println("Bots in range: " + botsInRange.get());
        }
        return currentPosition.getDistance();
    }

    private long botsInRange(Point3D point) {
        return nanoBots.stream().filter(b -> b.isWithinRange(point)).count();
    }
}
