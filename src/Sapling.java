import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Sapling extends Plant {
    private int healthLimit;

    private static final int TREE_ANIMATION_MAX = 600;
    private static final int TREE_ANIMATION_MIN = 50;
    private static final int TREE_ACTION_MAX = 1400;
    private static final int TREE_ACTION_MIN = 1000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;

    public Sapling(String id,
                   Point position,
                   List<PImage> images,
                   int actionPeriod,
                   int animationPeriod,
                   int health,
                   int healthLimit) {
        super(id, position, images, animationPeriod, actionPeriod, health);
        this.healthLimit = healthLimit;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        changeHealth(1);
        super.executeActivity(world, imageStore, scheduler);
    }

    public boolean transform(
            WorldModel world,
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
        } else if (getHealth() >= healthLimit) {
            Animator tree = Factory.createTree("tree_" + getId(),
                    getPosition(),
                    getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN),
                    getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
                    getNumFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN),
                    imageStore.getImageList("cake"));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            ((Tree) tree).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    private static int getNumFromRange(int max, int min)
    {
        Random rand = new Random();
        return min + rand.nextInt(
                max
                        - min);
    }
}
