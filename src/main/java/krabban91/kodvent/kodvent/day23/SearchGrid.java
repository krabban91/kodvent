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

    public Set<SearchGrid> createGridsWithLowerScale(int scaleFactor) {
        int newScale = this.scale / scaleFactor;

        if (newScale <= 1) {
            return Collections.singleton(new SearchGrid(this, 1, this.scaleStepSize));
        }
        Set<SearchGrid> newGrids = IntStream.range(0, scaleFactor).mapToObj(z ->
                IntStream.range(0, scaleFactor).mapToObj(y ->
                        IntStream.range(0, scaleFactor)
                                .mapToObj(x -> new SearchGrid(
                                        new Point3D(getMinX() + newScale * x, getMinY() + newScale * y, getMinZ() + newScale * z),
                                        new Point3D(getMinX() + newScale * (x + 1), getMinY() + newScale * (y + 1), getMinY() + newScale * (z + 1)),
                                        newScale, scaleFactor
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

        // bot in grid
        // or
        // closest corner in bot radius
        // or
        // closest corner in bounding box
        // and
        // grid radius limits inside grid.
        if (!isGridStrictlyOutsideOfRadius(bot) &&(isBotInsideGrid(bot) || isClosestCornerInsideRadius(bot))) {
            return true;
        }

        //       Either the circle's centre lies inside the rectangle, or
        //       One of the edges of the rectangle has a point in the circle.
        if (!isClosestCornerInsideBotBoundingCube(bot)) {
            //return false;
        }

        boolean gridMightIntersect = gridPartiallyWrapsBotRadius(bot);

        SearchGrid grid = boundingCubeIntersectsWithGrid(bot);
        boolean gridInterSects = grid != null && grid.anyGridEdgeIsInRadius(bot);

        return (//gridMightIntersect &&
               // (scale > 10000000 || gridInterSects) &&
               // (scale > 10000000 ||
                        (bot.getLocationsOnManhattanDistanceLimitsAdjustedToScale(scale, scaleStepSize).stream()
                        .anyMatch(this::isInsideGrid)));
    }

    public boolean isInsideGrid(Point3D p) {
        return getX(p) >= getMinX() && getX(p) <= getMaxX() &&
                getY(p) >= getMinY() && getY(p) <= getMaxY() &&
                getZ(p) >= getMinY() && getZ(p) <= getMaxZ();
    }

    private boolean gridPartiallyWrapsBotRadius(NanoBot bot) {
        int wrappingAxises = 0;
        if (getMinX() <= bot.getX() && bot.getX() <= getMaxX()) {
            wrappingAxises++;
        }
        if (getMinY() <= bot.getY() && bot.getY() <= getMaxY()) {
            wrappingAxises++;
        }
        if (getMinZ() <= bot.getZ() && bot.getZ() <= getMaxZ()) {
            wrappingAxises++;
        }

        return wrappingAxises >= 1;
    }

    public boolean isBotInsideGrid(NanoBot bot) {
        return isInsideGrid(bot.getCoordinate());//       Either the circle's centre lies inside the rectangle, or
    }

    public boolean isContainedInRadiusOf(NanoBot bot) {
        if (!isfullyContainedInBoundingCubeOf(bot)) {
            return false;
        }
        // check if singleCorner
        return isfullyContainedInBoundingCubeOf(bot) && isClosestCornerInsideRadius(bot);
    }

    private boolean isfullyContainedInBoundingCubeOf(NanoBot bot) {
        int botMinX = bot.getX() - bot.getSignalRadius();
        int botMaxX = bot.getX() + bot.getSignalRadius();
        boolean isInsideX = botMinX <= getMinX() && botMaxX >= getMaxX();
        int botMinY = bot.getY() - bot.getSignalRadius();
        int botMaxY = bot.getY() + bot.getSignalRadius();
        boolean isInsideY = botMinY <= getMinY() && botMaxY >= getMaxY();
        int botMinZ = bot.getZ() - bot.getSignalRadius();
        int botMaxZ = bot.getZ() + bot.getSignalRadius();
        boolean isInsideZ = botMinZ <= getMinZ() && botMaxZ >= getMaxZ();
        return isInsideX && isInsideY && isInsideZ;
    }

    public boolean isGridInsideRadiusOf(NanoBot bot) {
        //       One of the edges of the rectangle has a point in the circle.
        //       3 dimensions -> 12 edges and the sphere.
        // edge case: A corner is inside:
        if (isClosestCornerInsideRadius(bot)) {
            return true;
        }
        if(isGridStrictlyOutsideOfRadius(bot)){
            return false;
        }
        SearchGrid grid = boundingCubeIntersectsWithGrid(bot);

        return grid != null && (scale >= 100000 || grid.anyGridEdgeIsInRadius(bot));
    }

    private SearchGrid boundingCubeIntersectsWithGrid(NanoBot bot) {
        int minX = Math.max(getCenterX() - getWidthFromCenter(), bot.getX() - bot.getSignalRadius());
        int maxX = Math.min(getCenterX() + getWidthFromCenter(), bot.getX() + bot.getSignalRadius());
        int minY = Math.max(getCenterY() - getHeightFromCenter(), bot.getY() - bot.getSignalRadius());
        int maxY = Math.min(getCenterY() + getHeightFromCenter(), bot.getY() + bot.getSignalRadius());
        int minZ = Math.max(getCenterZ() - getDepthFromCenter(), bot.getZ() - bot.getSignalRadius());
        int maxZ = Math.min(getCenterZ() + getDepthFromCenter(), bot.getZ() + bot.getSignalRadius());
        if(minX <= maxX && minY <= maxY && minZ <= maxZ){
            return new SearchGrid(
                    new Point3D(minX, minY, minZ),
                    new Point3D(maxX, maxY, maxZ),
                    Math.min(maxZ - minZ, Math.min(maxX - minX, maxY - minY)),
                    this.scaleStepSize);
        }
        return null;
        //2d calculation:
        // left = max(r1.left, r2.left)
        // right = min(r1.right, r2.right)
        // bottom = min(r1.bottom, r2.bottom)
        // top = max(r1.top, r2.top)
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
        Set<Point3D> e1x = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMinX(), getMinY(), getMinZ()),
                new Point3D(getMaxX(), getMinY(), getMinZ()));
        Set<Point3D> e2x = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMinX(), getMaxY(), getMinZ()),
                new Point3D(getMaxX(), getMaxY(), getMinZ()));
        Set<Point3D> e3x = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMinX(), getMinY(), getMaxZ()),
                new Point3D(getMaxX(), getMinY(), getMaxZ()));
        Set<Point3D> e4x = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMinX(), getMaxY(), getMaxZ()),
                new Point3D(getMaxX(), getMaxY(), getMaxZ()));
        e1x.addAll(e2x);
        e1x.addAll(e3x);
        e1x.addAll(e4x);
        return e1x;
    }

    private Set<Point3D> getPointsInSegmentAlongY() {
        Set<Point3D> e1y = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMinX(), getMinY(), getMinZ()),
                new Point3D(getMinX(), getMaxY(), getMinZ()));
        Set<Point3D> e2y = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMaxX(), getMinY(), getMinZ()),
                new Point3D(getMaxX(), getMaxY(), getMinZ()));
        Set<Point3D> e3y = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMinX(), getMinY(), getMaxZ()),
                new Point3D(getMinX(), getMaxY(), getMaxZ()));
        Set<Point3D> e4y = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMaxX(), getMinY(), getMaxZ()),
                new Point3D(getMaxX(), getMaxY(), getMaxZ()));
        e1y.addAll(e2y);
        e1y.addAll(e3y);
        e1y.addAll(e4y);
        return e1y;
    }

    private Set<Point3D> getPointsInSegmentAlongZ() {
        Set<Point3D> e1z = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMinX(), getMinY(), getMinZ()),
                new Point3D(getMinX(), getMinY(), getMaxZ()));
        Set<Point3D> e2z = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMaxX(), getMinY(), getMinZ()),
                new Point3D(getMaxX(), getMinY(), getMaxZ()));
        Set<Point3D> e3z = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMinX(), getMaxY(), getMinZ()),
                new Point3D(getMinX(), getMaxY(), getMaxZ()));
        Set<Point3D> e4z = getPointsOnEdgeWithRegardToScale(
                new Point3D(getMaxX(), getMaxY(), getMinZ()),
                new Point3D(getMaxX(), getMaxY(), getMaxZ()));
        e1z.addAll(e2z);
        e1z.addAll(e3z);
        e1z.addAll(e4z);
        return e1z;
    }

    private Set<Point3D> getPointsOnEdgeWithRegardToScale(Point3D p1, Point3D p2) {
        return IntStream.rangeClosed(getZ(p1), getZ(p2))
                .filter(z -> z % (Math.max(4, scale / 16)) == 0)
                .mapToObj(z -> IntStream.rangeClosed(getY(p1), getY(p2))
                        .filter(y -> y % (Math.max(4, scale / 16)) == 0)
                        .mapToObj(y -> IntStream.rangeClosed(getX(p1), getX(p2))
                                .filter(x -> x % (Math.max(4, scale / 16)) == 0)
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

    public static int getX(Point3D p) {
        return (int) p.getX();
    }

    public static int getY(Point3D p) {
        return (int) p.getY();
    }

    public static int getZ(Point3D p) {
        return (int) p.getZ();
    }

    private boolean isClosestCornerInsideBotBoundingCube(NanoBot bot) {
        Point3D closest = closestCornerToBot(bot);
        int botMinX = bot.getX() - bot.getSignalRadius();
        int botMaxX = bot.getX() + bot.getSignalRadius();
        boolean isInsideX = botMinX <= getX(closest) && botMaxX >= getX(closest);
        int botMinY = bot.getY() - bot.getSignalRadius();
        int botMaxY = bot.getY() + bot.getSignalRadius();
        boolean isInsideY = botMinY <= getY(closest) && botMaxY >= getY(closest);
        int botMinZ = bot.getZ() - bot.getSignalRadius();
        int botMaxZ = bot.getZ() + bot.getSignalRadius();
        boolean isInsideZ = botMinZ <= getZ(closest) && botMaxZ >= getZ(closest);
        return isInsideX && isInsideY && isInsideZ;
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
