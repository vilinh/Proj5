public class Node implements Comparable<Node> {
    public Point p;
    private double g;
    private double f;
    private double h;
    private Node prior;

    public Node(Point p, double g, double h, double f, Node prior) {
        this.p = p;
        this.g = g;
        this.f = f;
        this.h = h;
        this.prior = prior;
    }

    public Node getPrior() {
        return prior;
    }

    public String toString() {
        return "(" + p.x + "," + p.y + ")";
    }

    public double getG() { return g; };

    public boolean equals(Object other) {
        return other instanceof Node && ((Node)other).p.x == this.p.x
                && ((Node)other).p.y == this.p.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + p.x;
        result = result * 31 + p.y;

        return result;
    }

    @Override
    public int compareTo(Node o) {
        if (this.f < o.f){
            return -1;
        } else if (this.f > o.f) {
            return 1;
        }
        return -1;
    }

}
