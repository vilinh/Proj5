import processing.core.PImage;

import java.util.*;

public class DudeFull extends Dudes {
    public DudeFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, animationPeriod, actionPeriod, resourceLimit);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore,
                                EventScheduler scheduler){
        Optional<Entity> fullTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(House.class)));

        if (fullTarget.isPresent() && moveTo(world,
                fullTarget.get(), scheduler))
        {
            transform(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }



    @Override
    public boolean moveTo(WorldModel world,
                          Entity target,
                          EventScheduler scheduler){
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

    private void transform(WorldModel world,
                           EventScheduler scheduler,
                           ImageStore imageStore){
        DudeNotFull miner = Factory.createDudeNotFull(getId(),
                getPosition(), getActionPeriod(),
                getAnimationPeriod(),
                getResourceLimit(),
                getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

}
