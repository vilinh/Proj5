import processing.core.PImage;

import java.util.List;

public abstract class Plant extends Activator{
    private int health;

    protected Plant(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int health) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.health = health;
    }

    public boolean transformPlant(WorldModel world,
                                  EventScheduler scheduler,
                                  ImageStore imageStore) {
        return transform(world, scheduler, imageStore);
    }
    abstract boolean transform(WorldModel world,
                               EventScheduler scheduler,
                               ImageStore imageStore);

    public int getHealth() {
        return health;
    }

    public void changeHealth(int x) {
        this.health += x;
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore,
                                EventScheduler scheduler) {
        if (!transformPlant(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }
}
