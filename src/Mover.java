import processing.core.PImage;

import java.util.List;

public abstract class Mover extends Activator {
    protected Mover(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    abstract Point nextPosition(WorldModel world, Point destPos);
    abstract boolean moveTo(WorldModel world,
                   Entity target,
                   EventScheduler scheduler);
    static boolean adjacent(Point p1, Point p2){
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) || (p1.y == p2.y
                && Math.abs(p1.x - p2.x) == 1);
    };
}
