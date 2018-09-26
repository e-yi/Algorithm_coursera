import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

    private LinkedList<LineSegment> segmentLinkedList = new LinkedList<>();

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points

        int len = points.length;
        for (int i = 0; i < len-3; i++) {
            Point a = points[i];
            Arrays.sort(points,a.slopeOrder());

            int count = 1;
            double last = a.slopeTo(points[i+1]);
            for (int k = 2; k < len - i - 1; k++) {
                double slope = a.slopeTo(points[i+k]);
                if (last == slope) {
                    count++;
                } else {
                    //last!=slope
                    if (count >= 3) {
                        segmentLinkedList.add(
                                new LineSegment(a, points[i + k])
                        );
                    }
                    count = 1;
                    last = slope;
                }
            }//end for

            if (count >= 3) {
                segmentLinkedList.add(
                        new LineSegment(a, points[len - 1])
                );
            }
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return segmentLinkedList.size();
    }

    public LineSegment[] segments() {
        // the line segments
        return segmentLinkedList.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}