package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SearchGrid {

    Point3D min;
    Point3D max;

    int scale;
    int scaleStepSize;

    public SearchGrid(Point3D min, Point3D max, int scale, int scaleStepSize) {
        this.min = min;
        this.max = max;
        this.scale = scale;
        this.scaleStepSize = scaleStepSize;
    }

    public SearchGrid(SearchGrid other, int scale, int scaleStepSize) {
        this.min = other.min;
        this.max = other.max;
        this.scale = scale;
        this.scaleStepSize = scaleStepSize;
    }

    public Stream<SearchGrid> createGridsWithLowerScale(int scaleFactor) {
        int newScale = this.scale / scaleFactor;
        int rest = this.scale % scaleFactor;
        if (newScale <= 1) {
            return Collections.singleton(new SearchGrid(this, 1, this.scaleStepSize)).stream();
        }
        Stream<SearchGrid> newGrids = IntStream.range(0, scaleFactor).parallel().mapToObj(z ->
                IntStream.range(0, scaleFactor).parallel().mapToObj(y ->
                        IntStream.range(0, scaleFactor).parallel()
                                .mapToObj(x -> new SearchGrid(
                                        new Point3D(getMinX() + newScale * x, getMinY() + newScale * y, getMinZ() + newScale * z),
                                        new Point3D(getMinX() + rest + newScale * (x + 1), getMinY() + rest + newScale * (y + 1), getMinZ() + rest + newScale * (z + 1)),
                                        newScale, scaleFactor)))
                        .reduce(Stream::concat)
                        .orElseGet(Stream::empty))
                .reduce(Stream::concat)
                .orElseGet(Stream::empty)
                .parallel();
        return newGrids;
    }

    public long botsWithinRange(List<NanoBot> bots) {
        return bots.stream().parallel().filter(this::isReachedByNanoBot).count();
    }

    public boolean isReachedByNanoBot(NanoBot bot) {

        // bot in grid
        // or
        // closest corner in bot radius
        // or
        // intersection of cubes
        if (!isGridStrictlyOutsideOfRadius(bot) && (isBotInsideGrid(bot) || isClosestCornerInsideRadius(bot))) {
            return true;
        }

        return boundingCubeIntersectsWithGridCube(bot);
    }

    public boolean isInsideGrid(Point3D p) {
        return getX(p) >= getMinX() && getX(p) <= getMaxX() &&
                getY(p) >= getMinY() && getY(p) <= getMaxY() &&
                getZ(p) >= getMinY() && getZ(p) <= getMaxZ();
    }

    public boolean isBotInsideGrid(NanoBot bot) {
        return isInsideGrid(bot.getCoordinate());//       Either the circle's centre lies inside the rectangle, or
    }

    private boolean boundingCubeIntersectsWithGridCube(NanoBot bot) {
        int gminX = getCenterX() - getWidthFromCenter();
        int cminX = bot.getX() - bot.getSignalRadius();
        int cmaxx = bot.getX() + bot.getSignalRadius();
        int cminY = bot.getY() - bot.getSignalRadius();
        int cmaxY = bot.getY() + bot.getSignalRadius();
        int cminZ = bot.getZ() - bot.getSignalRadius();
        int cmaxZ = bot.getZ() + bot.getSignalRadius();
        int gmaxX = getCenterX() + getWidthFromCenter();
        int minX = Math.max(gminX, cminX);
        int maxX = Math.min(gmaxX, cmaxx);
        int gminY = getCenterY() - getHeightFromCenter();
        int gmaxY = getCenterY() + getHeightFromCenter();
        int minY = Math.max(gminY, cminY);
        int maxY = Math.min(gmaxY, cmaxY);
        int gminZ = getCenterZ() - getDepthFromCenter();
        int gmaxZ = getCenterZ() + getDepthFromCenter();
        int minZ = Math.max(gminZ, cminZ);
        int maxZ = Math.min(gmaxZ, cmaxZ);
        return minX <= maxX && minY <= maxY && minZ <= maxZ;
    }

    private int getWidthFromCenter() {
        return (getMaxX() - getMinX()) / 2;
    }

    private int getHeightFromCenter() {
        return (getMaxY() - getMinY()) / 2;
    }

    private int getDepthFromCenter() {
        return (getMaxZ() - getMinZ()) / 2;
    }

    private int getCenterX() {
        return (getMaxX() + getMinX()) / 2;
    }

    private int getCenterY() {
        return (getMaxY() + getMinY()) / 2;
    }

    private int getCenterZ() {
        return (getMaxZ() + getMinZ()) / 2;
    }

    private boolean isGridStrictlyOutsideOfRadius(NanoBot bot) {
        int closestX = Math.abs(bot.getX() - getMinX()) < Math.abs(bot.getX() - getMaxX()) ? getMinX() : getMaxX();
        int closestY = Math.abs(bot.getY() - getMinY()) < Math.abs(bot.getY() - getMaxY()) ? getMinY() : getMaxY();
        int closestZ = Math.abs(bot.getZ() - getMinZ()) < Math.abs(bot.getZ() - getMaxZ()) ? getMinZ() : getMaxZ();

        return (bot.getX() - bot.getSignalRadius() > closestX || bot.getX() + bot.getSignalRadius() < closestX) ||
                (bot.getY() - bot.getSignalRadius() > closestY || bot.getY() + bot.getSignalRadius() < closestY) ||
                (bot.getZ() - bot.getSignalRadius() > closestZ || bot.getZ() + bot.getSignalRadius() < closestZ);
    }

    public Set<Point3D> getPointsInGrid() {
        return IntStream.rangeClosed(getMinZ(), getMaxZ())
                .mapToObj(z -> IntStream.rangeClosed(getMinY(), getMaxY())
                        .mapToObj(y -> IntStream.rangeClosed(getMinX(),getMaxX())
                                .mapToObj(x -> new Point3D(x, y, z))
                                .collect(Collectors.toSet()))
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public static int getX(Point3D p) {
        return (int) p.getX();
    }

    public static int getY(Point3D p) {
        return (int) p.getY();
    }

    public static int getZ(Point3D p) {
        return (int) p.getZ();
    }


    private boolean isClosestCornerInsideRadius(NanoBot bot) {
        Point3D point3D = closestCornerToBot(bot);
        return bot.isWithinRange(point3D);
    }

    private Point3D closestCornerToBot(NanoBot bot) {
        int closestX = Math.abs(bot.getX() - getMinX()) < Math.abs(bot.getX() - getMaxX()) ? getMinX() : getMaxX();
        int closestY = Math.abs(bot.getY() - getMinY()) < Math.abs(bot.getY() - getMaxY()) ? getMinY() : getMaxY();
        int closestZ = Math.abs(bot.getZ() - getMinZ()) < Math.abs(bot.getZ() - getMaxZ()) ? getMinZ() : getMaxZ();
        return new Point3D(closestX, closestY, closestZ);
    }

    public int getMinX() {
        return (int) this.min.getX();
    }

    public int getMinY() {
        return (int) this.min.getY();
    }

    public int getMinZ() {
        return (int) this.min.getZ();
    }

    public int getMaxX() {
        return (int) this.max.getX();
    }

    public int getMaxY() {
        return (int) this.max.getY();
    }

    public int getMaxZ() {
        return (int) this.max.getZ();
    }

    public static int manhattandistanceBetween(Point3D one, Point3D other) {
        return (int) (Math.abs(other.getX() - one.getX()) +
                Math.abs(other.getY() - one.getY()) +
                Math.abs(other.getZ() - one.getZ()));
    }

    public int getScale() {
        return scale;
    }
}
