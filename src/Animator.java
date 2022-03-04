import processing.core.PImage;

import java.util.List;

public abstract class Animator extends Entity {
    private int imageIndex;
    private final int animationPeriod;

    protected Animator(String id, Point position, List<PImage> images, int animationPeriod) {
        super(id, position, images);
        this.imageIndex = 0;
        this.animationPeriod = animationPeriod;
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, Factory.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }
    public int getAnimationPeriod() {
        return animationPeriod;
    }
    public void nextImage() {
        imageIndex = (imageIndex + 1) % getImages().size();
    }
    public PImage getCurrentImage() {
        return getImages().get(imageIndex);
    }
}
