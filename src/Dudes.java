import processing.core.PImage;

import java.util.List;
import java.util.function.Predicate;

public abstract class Dudes extends Mover {
    private final int resourceLimit;

    protected Dudes(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
    }

    public Point nextPosition(WorldModel world, Point destPos){

//        PathingStrategy strategy = new SingleStepPathingStrategy();
        AStarPathingStrategy strategy = new AStarPathingStrategy();

        Predicate<Point> pass = p -> world.withinBounds(p) && (!world.isOccupied(p) || world.getOccupancyCell(p).getClass() == Stump.class);

        List<Point> path = strategy.computePath(getPosition(), destPos,
                pass,
                (p1, p2) -> adjacent(p1, p2), PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0) {
            return getPosition();
        }

        return path.get(0);
    }

    public int getResourceLimit() {
        return resourceLimit;
    }
}
