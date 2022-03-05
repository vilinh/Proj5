import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Zenitsu extends Mover {

    public Zenitsu(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public void executeActivity(WorldModel world,
                                 ImageStore imageStore,
                                 EventScheduler scheduler){
        Optional<Entity> zenTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(Demon.class)));

        if (zenTarget.isPresent()) {
            moveTo(world, zenTarget.get(), scheduler);
        }
        else{
            ArrayList dnf = new ArrayList<>(Arrays.asList(DudeNotFull.class));
            ArrayList df = new ArrayList<>(Arrays.asList(DudeFull.class));
            dnf.addAll(df);
            Optional<Entity> zenTarget1 =
                    world.findNearest(getPosition(), dnf);

            if (zenTarget1.isPresent()) {
                moveTo(world, zenTarget1.get(), scheduler);
            }
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                getActionPeriod());
    }
    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Mover.adjacent(getPosition(), target.getPosition())) {

            if (target instanceof Demon){
                Demon t = (Demon) target;
                t.setHealth(-1);
            }
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



}
