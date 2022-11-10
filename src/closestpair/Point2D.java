/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package closestpair;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * A point in a 2D space
*/
public class Point2D {
    // Random number generator for generating random points
    private static Random rng;

    private final int x;
    private final int y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /*
     * Finds the euclidean distance between this point and other point.
    */
    public double distanceTo(Point2D point) {
        return Math.sqrt(Math.pow(point.x - this.x, 2) + Math.pow(point.y - this.y, 2));
    }

    /*
     * Compares this point to another point. Returns true if this point's x value is smaller or if both x values are equal and this point's y value is smaller
    */
    public boolean smallerThan(Point2D point) {
        return this.getX() < point.getX() || (this.getX() == point.getX() && this.getY() < point.getY());
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    /*
     * Now two points are equal if their x value and y values are equal
    */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        Point2D oth = (Point2D) other;

        return this.x == oth.x && this.y == oth.y;
    }

    /*
     * Hashes point based on x and y
    */
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    /*
     * Generates a random point with coordinates smaller than the given values
    */
    public static Point2D randomPoint(int x, int y) {
        rng = new Random();
        return new Point2D(rng.nextInt(2 * x) - x, rng.nextInt(2 * y) - y);
    }

    /*
     * Sorts an arraylist of points using quicksort
    */
    public static void sort(ArrayList<Point2D> points) {
        quicksort(points, 0, points.size());
    }

    /*
     * Sorts an arraylist of points using quicksort between the given bounds. End is exclusive.
    */
    private static void quicksort(ArrayList<Point2D> points, int start, int end) {
        if (start + 1 < end) {
            // Sorts based on pivot
            int pivotPosition = partition(points, start, end);

            // Sorts each partition at both sides of the pivot
            quicksort(points, start, pivotPosition);
            quicksort(points, pivotPosition + 1, end);
        }
    }

    /*
     * Swaps 2 points in an arraylist
    */
    private static void swap(ArrayList<Point2D> points, int index1, int index2) {
        Point2D temp = points.get(index1);
        points.set(index1, points.get(index2));
        points.set(index2, temp);
    }

    /*
     * Finds the pivot of a quicksort algorithm by using the median of the first, last and middle elements. The median is then placed at the end of the arraylist
    */
    public static Point2D median3(ArrayList<Point2D> points, int start, int end) {
        int mid = start + (end - start) / 2;
        Point2D first = points.get(start);
        Point2D middle = points.get(mid);
        Point2D last = points.get(end - 1);

        if (middle.smallerThan(first) ^ last.smallerThan(first)) {
            // The first element is the pivot
            swap(points, start, end - 1);
            return first;
        } else if (first.smallerThan(middle) ^ last.smallerThan(middle)) {
            // The middle element is the pivot
            swap(points, mid, end - 1);
            return middle;
        } else
            // The last element is the pivot
            return last;
    }

    /*
     * Sorts and partitions an sublist based on a pivot. Returns the position at which the pivot ends.
    */
    private static int partition(ArrayList<Point2D> points, int start, int end) {
        if (end - start == 2) {
            if (points.get(start + 1).smallerThan(points.get(start))) {
                swap(points, start, start + 1);
            }

            return start;
        }

        // Finds the pivot and temporarily places it at the end of the sublist
        Point2D pivot = median3(points, start, end);

        // The position in which the pivot will be placed at the end
        int pivotPosition = start;

        // Loops through the sublist
        for (int i = start; i < end; i++) {
            // If the element is smaller than the pivot it gets placed at the current pivot position. The pivot position then is incremented by 1.
            if (points.get(i).smallerThan(pivot)) {
                swap(points, i, pivotPosition);
                pivotPosition++;
            }
        }

        // In the end, the pivot is placed in its respective position
        swap(points, end - 1, pivotPosition);

        return pivotPosition;
    }
}
