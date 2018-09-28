import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private ArrayList<Line> segmentList = new ArrayList<>();

    private class Line{
        Point min;
        Point max;
        double slope;

        Line(Point min,Point max){
            this.min=min;
            this.max=max;
            this.slope=min.slopeTo(max);
        }
    }

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points

        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p:points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }
        points = points.clone();
        for (int i = 0; i < points.length; i++) {

        }
        Arrays.sort(points);

        Point p = null;
        for(Point point:points){
            if (point.equals(p)){
                throw new IllegalArgumentException();
            }
            p=point;
        }

        Point[] pointArr = points.clone();
        int len = points.length;
        for (int i = 0; i < len-1; i++) {
            Point a = points[i];
            Arrays.sort(pointArr,a.slopeOrder());
            int count = 1;
            double last = a.slopeTo(pointArr[1]);
            for (int k = 2; k < len ; k++) {
                double slope = a.slopeTo(pointArr[k]);
                if (last == slope) {
                    count++;
                } else {
                    //last!=slope
                    if (count >= 3) {
                        addSegment(a,pointArr,k-count,k-1);
                    }
                    count = 1;
                    last = slope;
                }
            }//end for

            if (count >= 3) {
                addSegment(a,pointArr,len-count,len-1);
            }
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return segmentList.size();
    }

    public LineSegment[] segments() {
        // the line segments
        LineSegment[] lineSegments = new LineSegment[segmentList.size()];
        for (int i = 0; i < lineSegments.length; i++) {
            Point min = segmentList.get(i).min;
            Point max = segmentList.get(i).max;
            lineSegments[i]=new LineSegment(min,max);
        }
        return lineSegments;
    }

    private void addSegment(Point p,Point[] points,int from,int to){
        Point min = p;
        Point max = p;
        for (int i=from;i<to+1;i++){
            Point pi = points[i];
            if (max.compareTo(pi)<0){
                max=pi;
            }else if (min.compareTo(pi)>0){
                min=pi;
            }
        }

        // 查看有没有已经在list里
        double slope = min.slopeTo(max);
        for (Line line:segmentList){
            if (line.slope==slope&&max==line.max){
                return;
            }
        }

        Line line = new Line(min,max);
        segmentList.add(line);
    }

//    public static void main(String[] args) {
//
//        // read the n points from a file
//        In in = new In(args[0]);
//        int n = in.readInt();
//        Point[] points = new Point[n];
//        for (int i = 0; i < n; i++) {
//            int x = in.readInt();
//            int y = in.readInt();
//            points[i] = new Point(x, y);
//        }
//
//        // draw the points
//        StdDraw.enableDoubleBuffering();
//        StdDraw.setXscale(0, 32768);
//        StdDraw.setYscale(0, 32768);
//        for (Point p : points) {
//            p.draw();
//        }
//        StdDraw.show();
//
//        // print and draw the line segments
//        FastCollinearPoints collinear = new FastCollinearPoints(points);
//        for (LineSegment segment : collinear.segments()) {
//            StdOut.println(segment);
//            segment.draw();
//        }
//        StdDraw.show();
//    }
}