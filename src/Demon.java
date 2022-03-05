import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Demon extends Mover {

    private int health;

    public Demon(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health)
    {
        super(id, position, images, animationPeriod, actionPeriod);
        this.health = health;
    }

    public void executeActivity(WorldModel world,
                                 ImageStore imageStore,
                                 EventScheduler scheduler){
        Optional<Entity> demonTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(DudeNotFull.class)));

        if (demonTarget.isPresent()) {
            moveTo(world, demonTarget.get(), scheduler);
        }

        if (!transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }

    }

    public boolean transform(WorldModel world,
                             EventScheduler scheduler,
                             ImageStore imageStore) {
        if (getHealth() <= 0) {

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            return true;
        }
        return false;
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Mover.adjacent(getPosition(), target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public Point nextPosition(WorldModel world, Point destPos)
    {
        AStarPathingStrategy strategy = new AStarPathingStrategy();

        Predicate<Point> pass = p -> !world.isOccupied(p) && world.withinBounds(p);

        List<Point> path = strategy.computePath(getPosition(), destPos,
                pass, (p1, p2) -> adjacent(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (path.size() == 0) {
            return getPosition();
        }
        return path.get(0);
    }

    public int getHealth(){
        return health;
    }
    public void setHealth(int h){
        health += h;
    }
}
