package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchGrid {

    Point3D min;
    Point3D max;

    int scale;

    public SearchGrid(Point3D min, Point3D max, int scale) {
        this.min = min;
        this.max = max;
        this.scale = scale;
    }

    public SearchGrid(SearchGrid other, int scale) {
        this.min = other.min;
        this.max = other.max;
        this.scale = scale;
    }

    public Set<SearchGrid> createGridsWithLowerScale(int scaleFactor) {
        int newScale = this.scale / scaleFactor;

        if (newScale <= 1) {
            return Collections.singleton(new SearchGrid(this, 1));
        }
        Set<SearchGrid> newGrids = IntStream.range(0, scaleFactor).mapToObj(z ->
                IntStream.range(0, scaleFactor).mapToObj(y ->
                        IntStream.range(0, scaleFactor)
                                .mapToObj(x -> new SearchGrid(
                                        new Point3D(getMinX() + newScale * x, getMinY() + newScale * y, getMinZ() + newScale * z),
                                        new Point3D(getMinX() + newScale * (x + 1), getMinX() + newScale * (y + 1), getMinX() + newScale * (z + 1)),
                                        newScale
                                ))
                                .collect(Collectors.toSet()))
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        return newGrids;
    }

    public long botsWithinRange(List<NanoBot> bots) {
        return bots.stream().filter(this::isReachedByNanoBot).count();
    }

    public boolean isReachedByNanoBot(NanoBot bot) {

        //       Either the circle's centre lies inside the rectangle, or
        //       One of the edges of the rectangle has a point in the circle.
        return isBotInsideGrid(bot) || isGridInsideRadiusOf(bot);
    }

    public boolean isBotInsideGrid(NanoBot bot) {
        //       Either the circle's centre lies inside the rectangle, or

        return (bot.getX() >= this.getMinX() && bot.getX() <= this.getMaxX()) &&
                (bot.getY() >= this.getMinY() && bot.getY() <= this.getMaxY()) &&
                (bot.getZ() >= this.getMinZ() && bot.getZ() <= this.getMaxZ());
    }

    public boolean isGridInsideRadiusOf(NanoBot bot) {
        //       One of the edges of the rectangle has a point in the circle.
        //       3 dimensions -> 12 edges and the sphere.
        // edge case: A corner is inside:
        if (isClosestCornerIsInsideRadius(bot)) {
            return true;
        }
        if(isGridStrictlyOutsideOfRadius(bot)){
            return false;
        }
        SearchGrid grid = boundingCubeIntersectsWithGrid(bot);

        return grid != null && (scale >= 1000 || grid.anyGridEdgeIsInRadius(bot));
    }

    private SearchGrid boundingCubeIntersectsWithGrid(NanoBot bot) {
        int minX = Math.max(getCenterX() - getWidthFromCenter(), bot.getX() - bot.getSignalRadius());
        int maxX = Math.min(getCenterX() + getWidthFromCenter(), bot.getX() + bot.getSignalRadius());
        int minY = Math.max(getCenterY() - getHeightFromCenter(), bot.getY() - bot.getSignalRadius());
        int maxY = Math.min(getCenterY() + getHeightFromCenter(), bot.getY() + bot.getSignalRadius());
        int minZ = Math.max(getCenterZ() - getDepthFromCenter(), bot.getZ() - bot.getSignalRadius());
        int maxZ = Math.min(getCenterZ() + getDepthFromCenter(), bot.getZ() + bot.getSignalRadius());
        if(minX <= maxX && minY <= maxY && minZ <= maxZ){
            return new SearchGrid(new Point3D(minX, minY, minZ), new Point3D(maxX, maxY, maxZ), this.scale);
        }
        return null;
        //2d calculation:
        // left = max(r1.left, r2.left)
        // right = min(r1.right, r2.right)
        // bottom = min(r1.bottom, r2.bottom)
        // top = max(r1.top, r2.top)
    }

    private Point3D getCenter() {
        return new Point3D(getCenterX(), getCenterY(), getCenterZ());
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

    private boolean anyGridEdgeIsInRadius(NanoBot bot) {
        Set<Point3D> pointsInSegmentAlongX = getPointsInSegmentAlongX();

        boolean matchOnX = pointsInSegmentAlongX.stream().anyMatch(p -> manhattandistanceBetween(bot.getCoordinate(), p) <= bot.getSignalRadius());
        if(matchOnX){
            return true;
        }
        Set<Point3D> pointsInSegmentAlongY = getPointsInSegmentAlongY();
        boolean matchOnY = pointsInSegmentAlongY.stream().anyMatch(p -> manhattandistanceBetween(bot.getCoordinate(), p) <= bot.getSignalRadius());
        if(matchOnY){
            return true;
        }
        Set<Point3D> pointsInSegmentAlongZ = getPointsInSegmentAlongZ();
        boolean matchOnZ = pointsInSegmentAlongZ.stream().anyMatch(p -> manhattandistanceBetween(bot.getCoordinate(), p) <= bot.getSignalRadius());
        return matchOnZ;
    }

    private Set<Point3D> getPointsInSegmentAlongX() {
        Set<Point3D> e1x = getPointsOnEdge(
                new Point3D(getMinX(), getMinY(), getMinZ()),
                new Point3D(getMaxX(), getMinY(), getMinZ()));
        Set<Point3D> e2x = getPointsOnEdge(
                new Point3D(getMinX(), getMaxY(), getMinZ()),
                new Point3D(getMaxX(), getMaxY(), getMinZ()));
        Set<Point3D> e3x = getPointsOnEdge(
                new Point3D(getMinX(), getMinY(), getMaxZ()),
                new Point3D(getMaxX(), getMinY(), getMaxZ()));
        Set<Point3D> e4x = getPointsOnEdge(
                new Point3D(getMinX(), getMaxY(), getMaxZ()),
                new Point3D(getMaxX(), getMaxY(), getMaxZ()));
        e1x.addAll(e2x);
        e1x.addAll(e3x);
        e1x.addAll(e4x);
        return e1x;
    }

    private Set<Point3D> getPointsInSegmentAlongY() {
        Set<Point3D> e1y = getPointsOnEdge(
                new Point3D(getMinX(), getMinY(), getMinZ()),
                new Point3D(getMinX(), getMaxY(), getMinZ()));
        Set<Point3D> e2y = getPointsOnEdge(
                new Point3D(getMaxX(), getMinY(), getMinZ()),
                new Point3D(getMaxX(), getMaxY(), getMinZ()));
        Set<Point3D> e3y = getPointsOnEdge(
                new Point3D(getMinX(), getMinY(), getMaxZ()),
                new Point3D(getMinX(), getMaxY(), getMaxZ()));
        Set<Point3D> e4y = getPointsOnEdge(
                new Point3D(getMaxX(), getMinY(), getMaxZ()),
                new Point3D(getMaxX(), getMaxY(), getMaxZ()));
        e1y.addAll(e2y);
        e1y.addAll(e3y);
        e1y.addAll(e4y);
        return e1y;
    }

    private Set<Point3D> getPointsInSegmentAlongZ() {
        Set<Point3D> e1z = getPointsOnEdge(
                new Point3D(getMinX(), getMinY(), getMinZ()),
                new Point3D(getMinX(), getMinY(), getMaxZ()));
        Set<Point3D> e2z = getPointsOnEdge(
                new Point3D(getMaxX(), getMinY(), getMinZ()),
                new Point3D(getMaxX(), getMinY(), getMaxZ()));
        Set<Point3D> e3z = getPointsOnEdge(
                new Point3D(getMinX(), getMaxY(), getMinZ()),
                new Point3D(getMinX(), getMaxY(), getMaxZ()));
        Set<Point3D> e4z = getPointsOnEdge(
                new Point3D(getMaxX(), getMaxY(), getMinZ()),
                new Point3D(getMaxX(), getMaxY(), getMaxZ()));
        e1z.addAll(e2z);
        e1z.addAll(e3z);
        e1z.addAll(e4z);
        return e1z;
    }

    private Set<Point3D> getPointsOnEdge(Point3D p1, Point3D p2) {
        return IntStream.rangeClosed(getZ(p1), getZ(p2))
                .mapToObj(z -> IntStream.rangeClosed(getY(p1), getY(p2))
                        .mapToObj(y -> IntStream.rangeClosed(getX(p1), getX(p2))
                                .mapToObj(x -> new Point3D(x, y, z))
                                .collect(Collectors.toSet()))
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
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

    private static int getX(Point3D p) {
        return (int) p.getX();
    }

    private static int getY(Point3D p) {
        return (int) p.getY();
    }

    private static int getZ(Point3D p) {
        return (int) p.getZ();
    }

    private boolean isClosestCornerIsInsideRadius(NanoBot bot) {
        int closestX = Math.abs(bot.getX() - getMinX()) < Math.abs(bot.getX() - getMaxX()) ? getMinX() : getMaxX();
        int closestY = Math.abs(bot.getY() - getMinY()) < Math.abs(bot.getY() - getMaxY()) ? getMinY() : getMaxY();
        int closestZ = Math.abs(bot.getZ() - getMinZ()) < Math.abs(bot.getZ() - getMaxZ()) ? getMinZ() : getMaxZ();
        Point3D point3D = new Point3D(closestX, closestY, closestZ);

        return manhattandistanceBetween(bot.getCoordinate(), point3D) <= bot.getSignalRadius();
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
