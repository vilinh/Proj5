import processing.core.PImage;

import java.util.List;

public abstract class Activator extends Animator {
    private int actionPeriod;
    protected Activator(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    abstract void executeActivity(WorldModel world,
                                  ImageStore imageStore,
                                  EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world,
                                ImageStore imageStore){
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }
    public int getActionPeriod() {
        return actionPeriod;
    }
}
