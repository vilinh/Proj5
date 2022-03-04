public final class Activity extends Action
{
    private final Activator activator;
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(
            Activator activator,
            WorldModel world,
            ImageStore imageStore)
    {
        this.activator = activator;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {
        activator.executeActivity(world, imageStore, scheduler);
    }

}
