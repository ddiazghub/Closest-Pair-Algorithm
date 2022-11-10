/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package closestpair;

import closestpair.result.ClosestPairResult;
import closestpair.result.SpacePartitionResult;
import java.util.ArrayList;

/**
 * A partition of points in a 2D space, having up to 3 points
*/
public class SpacePartition2D {
    private final ArrayList<Point2D> points;

    // Partition bounds in the x axis
    public final int minX;
    public final int maxX;

    public SpacePartition2D(ArrayList<Point2D> points) {
        this.points = points;
        this.minX = points.get(0).getX();
        this.maxX = points.get(points.size() - 1).getX();
    }

    public SpacePartition2D(ArrayList<Point2D> points, int start, int end) {
        this(new ArrayList<>(points.subList(start, end)));
    }

    /**
     * Finds the closest pair of points in the partition using brute force
    */
    public ClosestPairResult closestPair() {
        Point2D point1 = null;
        Point2D point2 = null;

        // Starts counting iterations, and elapsed time
        long iterations = 0;
        long startTime = System.nanoTime();
        long minimunDistance = Long.MAX_VALUE;
        int end = this.points.size();

        // Compares each point to all other points and finds the distance
        for (int i = 0; i < end - 1; i++) {
            Point2D first = this.points.get(i);

            for (int j = i + 1; j < end; j++) {
                iterations++;
                Point2D second = points.get(j);
                long distance = first.distanceTo(second);

                // If the distance is smaller than the minimun distance it becomes the new closest pair
                if (distance < minimunDistance) {
                    point1 = first;
                    point2 = second;
                    minimunDistance = distance;
                }
            }
        }

        return new ClosestPairResult(point1, point2, minimunDistance, iterations, System.nanoTime() - startTime);
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Space Partition[ ");

        for (Point2D point : this.points) {
            string.append(point).append(" ");
        }

        string.append("]");

        return string.toString();
    }

    /**
     * Partitions a list of points into a list of space partitions
    */
    public static SpacePartitionResult partition(ArrayList<Point2D> points) {
        long start = System.nanoTime();
        SpacePartitionResult result = partition(points, 0, points.size());

        return new SpacePartitionResult(result.getPartitions(), result.getIterations(), System.nanoTime() - start);
    }

    /**
     * Partitions a sublist of points into a list of space partitions
    */
    public static SpacePartitionResult partition(ArrayList<Point2D> points, int start, int end) {
        // If the number of points in the sublist is 3 or smaller, creates a new partition
        if (end - start < 4) {
            ArrayList<SpacePartition2D> list = new ArrayList<>();
            list.add(new SpacePartition2D(points, start, end));

            return new SpacePartitionResult(list, 1, 0);
        }

        // Splits the sublist in half and partitions both halves
        int mid = start + (end - start) / 2;
        SpacePartitionResult result1 = partition(points, start, mid);
        SpacePartitionResult result2 = partition(points, mid, end);
        result1.getPartitions().addAll(result2.getPartitions());

        return new SpacePartitionResult(result1.getPartitions(), 1 + result1.getIterations() + result2.getIterations(), 0);
    }
}