import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private final Point2D point;
        private Node big;
        private Node small;

        Node(Point2D p) {
            this.point = p;
        }

        void insert(Point2D point, boolean left) {
            if (left) {
                this.big = new Node(point);
            } else {
                this.small = new Node(point);
            }
        }
    }

    private static class SplitSpace{
        private final RectHV rightOrUp;
        private final RectHV leftOrDown;

        SplitSpace(Node node,RectHV space, boolean isHorizontal){
            if (isHorizontal){
                // up (y axis going upwards
                rightOrUp = new RectHV(space.xmin(),node.point.y(),space.xmax(),space.ymax());
                //down
                leftOrDown = new RectHV(space.xmin(),space.ymin(),space.xmax(),node.point.y());
            }else{
                // right
                rightOrUp = new RectHV(node.point.x(),space.ymin(),space.xmax(),space.ymax());
                // left
                leftOrDown = new RectHV(space.xmin(),space.ymin(),node.point.x(),space.ymax());
            }
        }
    }

    public KdTree() {
        // construct an empty set of points
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        // is the set empty?
        return size == 0;
    }

    public int size() {
        // number of points in the set
        return size;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            root = new Node(p);
            size++;
            return;
        }
        insert(root, p, false);
    }

    /**
     * @param node
     * @param insertP
     * @param isHorizontal 当前结点node是水平分割还是垂直分割区域
     */
    private void insert(Node node, Point2D insertP, boolean isHorizontal) {
        Point2D point = node.point;
        switch (compare(insertP, point, isHorizontal)) {
            case 1:
                if (node.big == null) {
                    node.insert(insertP, true);
                    size += 1;
                } else {
                    insert(node.big, insertP, !isHorizontal);
                }
                break;
            case -1:
                if (node.small == null) {
                    node.insert(insertP, false);
                    size += 1;
                } else {
                    insert(node.small, insertP, !isHorizontal);
                }
                break;
            case 0:
                break;
            default:
                throw new RuntimeException("Impossible Exception Happened.");
        }
    }

    private int compare(Point2D p1, Point2D p2, boolean yFirst) {
        if (yFirst) {
            if (p1.y() < p2.y()) {
                return -1;
            }
            if (p1.y() > p2.y()) {
                return +1;
            }
            if (p1.x() < p2.x()) {
                return -1;
            }
            if (p1.x() > p2.x()) {
                return +1;
            }
        } else {
            if (p1.x() < p2.x()) {
                return -1;
            }
            if (p1.x() > p2.x()) {
                return +1;
            }
            if (p1.y() < p2.y()) {
                return -1;
            }
            if (p1.y() > p2.y()) {
                return +1;
            }
        }
        return 0;
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return false;
        }
        return subTreeContains(root, p, false);
    }

    private boolean subTreeContains(Node node, Point2D p, boolean isHorizontal) {
        Point2D point = node.point;
        switch (compare(p, point, isHorizontal)) {
            case 1:
                if (node.big == null) {
                    return false;
                }
                return subTreeContains(node.big, p, !isHorizontal);
            case -1:
                if (node.small == null) {
                    return false;
                }
                return subTreeContains(node.small, p, !isHorizontal);
            case 0:
                return true;
            default:
                throw new RuntimeException("Impossible Exception Happened.");
        }
    }

    /**
     * assume the first point split the space vertically
     */
    public void draw() {
        // draw all points to standard draw
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        if (root == null) {
            return;
        }

        RectHV full = new RectHV(0, 0, 1, 1);
        draw(root, false, full);
    }

    private void draw(Node node, boolean isHorizontal, RectHV space) {
        //draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        //draw line
        StdDraw.setPenColor(isHorizontal?StdDraw.BLUE:StdDraw.RED);
        StdDraw.setPenRadius();
        double x1,x2,y1,y2;
        if (isHorizontal){
            y1 = y2 = node.point.y();
            x1 = space.xmin();
            x2 = space.xmax();
        }else{
            y1 = space.ymin();
            y2 = space.ymax();
            x1 = x2 = node.point.x();
        }
        StdDraw.line(x1,y1,x2,y2);

        SplitSpace splitSpace = new SplitSpace(node,space,isHorizontal);

        //next
        if (node.big != null){
            draw(node.big,!isHorizontal,splitSpace.rightOrUp);
        }
        if (node.small != null){
            draw(node.small,!isHorizontal,splitSpace.leftOrDown);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()){
            return new Bag<>();
        }

        //BFS
        Stack<Node> stack = new Stack<>();
        Bag<Point2D> pointBag = new Bag<>();
        boolean isHorizontal = false;

        stack.push(root);
        while (!stack.isEmpty()){
            Node node = stack.pop();
            Point2D point = node.point;

            int relativePosition = inRect(point,rect,isHorizontal);

            if (relativePosition==0){
                if (inRect(point,rect,!isHorizontal)==0){
                    pointBag.add(point);
                }
                if (node.big != null) {
                    stack.push(node.big);
                }
                if (node.small !=null){
                    stack.push(node.small);
                }
            }else if (relativePosition<0){
                //point<rect
                if (node.big != null) {
                    stack.push(node.big);
                }
            }else {
                if (node.small !=null){
                    stack.push(node.small);
                }
            }

            isHorizontal = !isHorizontal;
        }

        return pointBag;
    }

    /**
     * just like `compare`
     * @param point
     * @param rect
     * @param testY
     * @return
     */
    private int inRect(Point2D point, RectHV rect, boolean testY){
        if (testY) {
            double y = point.y();
            if (y>rect.ymax()){
                return 1;
            }
            if (y<rect.ymin()){
                return -1;
            }
            return 0;
        }else {
            double x = point.x();
            if (x > rect.xmax()) {
                return 1;
            }
            if (x < rect.xmin()) {
                return -1;
            }
            return 0;
        }
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()){
            return null;
        }
        if (size==1){
            return root.point;
        }

        RectHV full = new RectHV(0,0,1,1);
        return subTreeNearest(root,p,full,false);
    }

    private Point2D subTreeNearest(Node node,Point2D target,RectHV space, boolean isHorizontal){
        Point2D nearestPoint = node.point;

        SplitSpace splitSpace = new SplitSpace(node,space,isHorizontal);

        if (node.big!=null && splitSpace.rightOrUp.contains(target)){
            Point2D nearestPointOfBig = subTreeNearest(node.big,target,splitSpace.rightOrUp,!isHorizontal);
            nearestPoint = target.distanceSquaredTo(nearestPoint)<=target.distanceSquaredTo(nearestPointOfBig)?
                    nearestPoint:nearestPointOfBig;
        }
        if (node.small!=null && splitSpace.leftOrDown.contains(target)){
            Point2D nearestPointOfSmall = subTreeNearest(node.small,target,splitSpace.leftOrDown,!isHorizontal);
            nearestPoint = target.distanceSquaredTo(nearestPoint)<=target.distanceSquaredTo(nearestPointOfSmall)?
                    nearestPoint:nearestPointOfSmall;
        }

        return nearestPoint;
    }


    public static void main(String[] args) {

        RectHV rect0 = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        KdTree kdtree = new KdTree();
        final int NUM_POINT = 5;
        while (true){
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect0.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    kdtree.insert(p);
                    StdDraw.clear();
                    kdtree.draw();
                    StdDraw.show();
                }
                if (kdtree.size()>=NUM_POINT){
                    StdDraw.pause(500);
                    StdDraw.clear();
                    StdDraw.pause(450);
                    break;
                }
            }
            StdDraw.pause(50);
        }

        double x0 = 0.0, y0 = 0.0;
        // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;
        // current location of mouse
        boolean isDragging = false;
        // is the user dragging a rectangle

        // process range search queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // user starts to drag a rectangle
            if (StdDraw.isMousePressed() && !isDragging) {
                x0 = x1 = StdDraw.mouseX();
                y0 = y1 = StdDraw.mouseY();
                isDragging = true;
            }

            // user is dragging a rectangle
            else if (StdDraw.isMousePressed() && isDragging) {
                x1 = StdDraw.mouseX();
                y1 = StdDraw.mouseY();
            }

            // user stops dragging rectangle
            else if (!StdDraw.isMousePressed() && isDragging) {
                isDragging = false;
            }

            // draw the points
            StdDraw.clear();
            kdtree.draw();

            // draw the rectangle
            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                    Math.max(x0, x1), Math.max(y0, y1));
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
            rect.draw();

            // draw the range search results for kd-tree in blue
            StdDraw.setPenRadius(0.015);
            StdDraw.setPenColor(StdDraw.BLUE);
            for (Point2D p : kdtree.range(rect)) {
                p.draw();
            }

            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}
