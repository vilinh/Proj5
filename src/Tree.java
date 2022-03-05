import processing.core.PImage;

import java.util.List;

public class Tree extends Plant {


    public Tree(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health) {
        super(id, position, images, animationPeriod, actionPeriod, health);
    }

    public boolean transform(WorldModel world,
                             EventScheduler scheduler,
                             ImageStore imageStore) {
        if (getHealth() <= 0) {
            Entity stump = Factory.createStump(getId(),
                    getPosition(),
                    imageStore.getImageList("cherry"));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }

}
