/*
 * Algorithms and Complexity                                August 5, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 * Name: David Eduardo Díaz de Moya
 *
 * Synopsis:
 * Finds the closest pair of points in a group by space partitioning.
 *
 *
 * Copyright (c) 2022 David Eduardo Díaz de Moya
 * This file is released under the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * References:
 * [0] Files: www.w3schools.com/java/java_files_create.asp
 * [1] BufferedReader: https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
 * [2] BufferedWriter: https://docs.oracle.com/javase/7/docs/api/java/io/BufferedWriter.html
 * [3] IOException: (docs.oracle.com/javase/7/docs/api/java/io/IOException.html)
 * [4] FileNotFoundException: (docs.oracle.com/javase/7/docs/api/java/io/FileNotFoundException.html)
 * [5] ArrayList: https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
 * [6] HashSet: https://docs.oracle.com/javase/7/docs/api/java/util/HashSet.html
 * [7] Random: (docs.oracle.com/javase/8/docs/api/java/util/Random.html)
 * [8] Objects: https://docs.oracle.com/javase/8/docs/api/java/util/Objects.html
 */
package closestpair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author dedemoya
 */
public class ClosestPair {
    /**
     * A point in a 2D space
    */
    public static class Point2D {
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
         * Compares this point to other point. Returns true if this point's x value is smaller or if both x values are equal and this point's y value is smaller
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
    
    /**
     * A partition of points in a 2D space, having up to 3 points
    */
    public static class SpacePartition2D {
        private final ArrayList<Point2D> points;
        
        // Partition bounds in the x axis
        public final int minX;
        public final int maxX;

        public SpacePartition2D(ArrayList<Point2D> points) {
            this.points = points;
            this.minX = points.get(0).x;
            this.maxX = points.get(points.size() - 1).x;
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
            int iterations = 0;
            long startTime = System.nanoTime();
            double minimunDistance = Double.MAX_VALUE;
            int end = this.points.size();
            
            // Compares each point to all other points and finds the distance
            for (int i = 0; i < end - 1; i++) {
                Point2D first = this.points.get(i);

                for (int j = i + 1; j < end; j++) {
                    iterations++;
                    Point2D second = points.get(j);
                    double distance = first.distanceTo(second);

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
        public static ArrayList<SpacePartition2D> partition(ArrayList<Point2D> points) {
            return partition(points, 0, points.size());
        }
        
        /**
         * Partitions a sublist of points into a list of space partitions
        */
        public static ArrayList<SpacePartition2D> partition(ArrayList<Point2D> points, int start, int end) {
            // If the number of points in the sublist is 3 or smaller, creates a new partition
            if (end - start < 4) {
                ArrayList<SpacePartition2D> list = new ArrayList<>();
                list.add(new SpacePartition2D(points, start, end));
                
                return list;
            }
            
            // Splits the sublist in half and partitions both halves
            int mid = start + (end - start) / 2;
            ArrayList<SpacePartition2D> list = partition(points, start, mid);
            list.addAll(partition(points, mid, end));
            
            return list;
        }
    }
    
    /**
     * Resulting data from the execution of a closest pair algorithm
    */
    public static class ClosestPairResult {
        private final Point2D point1;
        private final Point2D point2;
        private final double distance;
        private final int iterations;
        private final long time;

        public ClosestPairResult(Point2D point1, Point2D point2, double distance, int iterations, long time) {
            this.point1 = point1;
            this.point2 = point2;
            this.iterations = iterations;
            this.time = time;
            this.distance = distance;
        }

        public Point2D getPoint1() {
            return point1;
        }

        public Point2D getPoint2() {
            return point2;
        }

        public int getIterations() {
            return iterations;
        }

        public long getTime() {
            return time;
        }

        public double getDistance() {
            return distance;
        }
    }
    
    /**
     * Finds the closest pair of points in a list of points
    */
    public static ClosestPairResult closestPair(ArrayList<Point2D> points) {
        Point2D point1 = null;
        Point2D point2 = null;
        
        // Starts counting iterations, and elapsed time
        int iterations = 0;
        long time = 0;
        double minimunDistance = Double.MAX_VALUE;
        
        // Splits the list of points into space partitions
        ArrayList<SpacePartition2D> partitions = SpacePartition2D.partition(points);
        
        // For each partition
        for (SpacePartition2D partition : partitions) {
            // Finds the closest pair in the partition
            ClosestPairResult result = partition.closestPair();
            iterations += result.getIterations();
            time += result.getTime();
            
            // If the distance of the partition's closest pair is smaller than the overall minimun distance it becomes the new overall closest pair
            if (result.getDistance() < minimunDistance) {
                minimunDistance = result.getDistance();
                point1 = result.getPoint1();
                point2 = result.getPoint2();
            }
        }
        
        // Loops through partitions to find closest pairs by combining partitions
        for (int i = 0; i < partitions.size() - 1; i++) {
            SpacePartition2D partition = partitions.get(i);
            ArrayList<Point2D> p = partition.getPoints();
            SpacePartition2D next = partitions.get(i + 1);
            ArrayList<Point2D> candidates = new ArrayList<>();
            
            // Finds all points in current partition that could be part of the closest pair
            for (int j = p.size() - 1; j > -1 && next.minX - p.get(j).getX() < minimunDistance; j--)
                candidates.add(p.get(j));

            // Finds all points in next partition that could be part of the closest pair
            for (int j = 0; j < next.getPoints().size() && next.getPoints().get(j).getX() - partition.maxX < minimunDistance; j++)
                candidates.add(next.getPoints().get(j));
            
            // If there are candidates for closest pair
            if (candidates.size() > 0 ) {
                // Finds minimun distance between candidates
                ClosestPairResult result = new SpacePartition2D(candidates).closestPair();
                iterations += result.getIterations();
                time += result.getTime();

                // If the distance is smaller than the minimun distance, it becomes the closest pair
                if (result.getDistance() < minimunDistance) {
                    minimunDistance = result.getDistance();
                    point1 = result.getPoint1();
                    point2 = result.getPoint2();
                }
            }
        }
        
        return new ClosestPairResult(point1, point2, minimunDistance, iterations, time);
    }
    
    /**
     * Class containing methods for handling input data
    */
    public static class InputFileHandler {
        /**
         * Creates a file containing random points
        */
        public static void create(String filename) {
            File file = new File(filename);
            
            try {
                // Creates file
                if (!file.exists())
                    file.delete();
            
                file.createNewFile();
                
                try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
                    // Writes 15 random unique points to the file
                    HashSet<Point2D> set = new HashSet<>();
                    
                    while (set.size() < 15) {
                        Point2D point = Point2D.randomPoint(100, 100);
                        
                        if (!set.contains(point)) {
                            set.add(point);
                            out.write(point.getX() + "," + point.getY() + "\n");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        /**
         * Creates a file containing points to an arraylist of points
        */
        public static ArrayList<Point2D> read(String filename) {
            ArrayList<Point2D> points = new ArrayList<>();
            File file = new File(filename);
            
            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                String line = in.readLine();
                
                while (line != null) {
                    String[] input = line.split(",");
                    Point2D point = new Point2D(Integer.parseInt(input[0]), Integer.parseInt(input[1]));
                    points.add(point);
                    line = in.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return points;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "data.txt";
        InputFileHandler.create(filename);
        ArrayList<Point2D> points = InputFileHandler.read(filename);
        Point2D.sort(points);
        
        for (SpacePartition2D partition : SpacePartition2D.partition(points)) {
            System.out.println(partition);
        }
        
        ClosestPairResult result = closestPair(points);
        
        System.out.println("The closest points are " + result.getPoint1() + " and " + result.getPoint2() + ".\n" + "Distance: " + result.getPoint1().distanceTo(result.getPoint2()));
    }
}
