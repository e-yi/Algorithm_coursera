import edu.princeton.cs.algs4.Quick;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.LinkedList;

public class BruteCollinearPoints {

    private LinkedList<LineSegment> segmentLinkedList=new LinkedList<>();

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p:points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }

        Quick.sort(points);
        //StdOut.println(java.util.Arrays.toString(points));
        int len = points.length;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                for (int k = j + 1; k < len; k++) {
                    for (int l = k + 1; l < len; l++) {
                        Point a = points[i];
                        Point b = points[j];
                        Point c = points[k];
                        Point d = points[l];
                        if (a.slopeTo(b) == b.slopeTo(c)
                                && b.slopeTo(c) == c.slopeTo(d)) {
                            segmentLinkedList.add(new LineSegment(a, d));
                        }
                    }
                }
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
        Point[] points = new Point[20];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(StdRandom.uniform(100), StdRandom.uniform(100));
        }
        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
    }
}