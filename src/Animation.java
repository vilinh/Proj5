public final class Animation extends Action
{
    private final Animator animator;
    private final int repeatCount;

    public Animation(
            Animator animator,
            int repeatCount)
    {
        this.animator = animator;

        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        animator.nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(animator,
                    Factory.createAnimationAction(animator,
                            Math.max(repeatCount - 1,
                                    0)),
                    animator.getAnimationPeriod());
        }
    }


}
