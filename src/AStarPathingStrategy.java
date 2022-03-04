import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Collections;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new ArrayList<>();

        PriorityQueue<Node> openQueue = new PriorityQueue<>();
        HashMap<Point, Node> open = new HashMap<>();
        HashMap<Point, Node> closed = new HashMap<>();

        Node curr = new Node(start, 0, 0,0,null);
        openQueue.add(curr);
        open.put(start, curr);

        while (!open.isEmpty()) {
            curr = openQueue.remove();
            open.remove(curr.p);

            if (withinReach.test(curr.p, end)) {
                while (curr.getPrior() != null) {
                    path.add(curr.p);
                    curr = curr.getPrior();
                }
                Collections.reverse(path);
                return path;
            }

            List<Point> neighbors = potentialNeighbors
                    .apply(curr.p)
                    .filter(canPassThrough)
                    .filter(p -> !p.equals(end))
                    .filter(p -> !closed.containsKey(p))
                    .collect(Collectors.toList());

            for (Point n : neighbors) {

                double newG = curr.getG() + 1;
                double newH = heur(n, end);
                double newF = newG + newH;

                Node newNode = new Node(n, newG, newH, newF, curr);

                if(open.containsKey(n)) {
                    if (newG < open.get(n).getG()){
                        //replace
                        openQueue.remove(open.get(n));
                        openQueue.add(newNode);
                        open.replace(n, newNode);
                    }
                } else {
                    openQueue.add(newNode);
                    open.put(n, newNode);
                }
            }
            closed.put(curr.p, curr);
        }

        return path;
    }

    public double heur(Point p1, Point p2){
        double dist = Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
        return dist;
    }

}
