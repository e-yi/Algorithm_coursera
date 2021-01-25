import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private final SET<Point2D> set;

    public PointSET() {
        // construct an empty set of points
        set = new SET<>();
    }

    public boolean isEmpty() {
        // is the set empty?
        return set.isEmpty();
    }

    public int size() {
        // number of points in the set
        return set.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }

        set.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return set.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Bag<Point2D> bag = new Bag<>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                bag.add(p);
            }
        }
        return bag;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearestP = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D point : set) {
            double distance = p.distanceSquaredTo(point);
            if (distance < minDistance) {
                nearestP = point;
                minDistance = distance;
            }
        }
        return nearestP;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        PointSET pointSET = new PointSET();
        pointSET.insert(new Point2D(0.5, 0.5));
        pointSET.insert(new Point2D(0.1, 0.1));
        pointSET.draw();
    }
}