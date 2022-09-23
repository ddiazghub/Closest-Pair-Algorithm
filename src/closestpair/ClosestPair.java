/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package closestpair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author dedemoya
 */
public class ClosestPair {
    public static class Point2D {
        private static Random rng;
        
        private final double x;
        private final double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
        
        public double distanceTo(Point2D point) {
            return Math.sqrt(Math.pow(point.x - this.x, 2) + Math.pow(point.y - this.y, 2));
        }
        
        public boolean smallerThan(Point2D point) {
            return this.getX() < point.getX() || (this.getX() == point.getX() && this.getY() < point.getY());
        }
        
        @Override
        public String toString() {
            return "(" + this.x + "," + this.y + ")";
        }
        
        public static Point2D randomPoint(double x, double y) {
            rng = new Random();
            return new Point2D(truncate2(2 * x * (rng.nextDouble() - 0.5)), truncate2(2 * y * (rng.nextDouble() - 0.5)));
        }
        
        public static Point2D median3(ArrayList<Point2D> points, int start, int end) {
            int mid = start + (end - start) / 2;
            Point2D first = points.get(start);
            Point2D middle = points.get(mid);
            Point2D last = points.get(end - 1);
            
            if (middle.smallerThan(first) ^ last.smallerThan(first)) {
                swap(points, start, end - 1);
                return first;
            } else if (first.smallerThan(middle) ^ last.smallerThan(middle)) {
                swap(points, mid, end - 1);
                return middle;
            } else
                return last;
        }
        
        public static void sort(ArrayList<Point2D> points) {
            quicksort(points, 0, points.size());
        }
        
        private static void quicksort(ArrayList<Point2D> points, int start, int end) {
            if (start + 1 < end) {
                int pivotPosition = partition(points, start, end);
                quicksort(points, start, pivotPosition);
                quicksort(points, pivotPosition + 1, end);
            }
        }
        
        private static void swap(ArrayList<Point2D> points, int index1, int index2) {
            Point2D temp = points.get(index1);
            points.set(index1, points.get(index2));
            points.set(index2, temp);
        }
        
        private static int partition(ArrayList<Point2D> points, int start, int end) {
            if (end - start == 2) {
                if (points.get(start + 1).smallerThan(points.get(start))) {
                    swap(points, start, start + 1);
                }
                
                return start;
            }
            
            Point2D pivot = median3(points, start, end);
            int pivotPosition = start;
            
            for (int i = start; i < end; i++) {
                if (points.get(i).smallerThan(pivot)) {
                    swap(points, i, pivotPosition);
                    pivotPosition++;
                }
            }
            
            swap(points, end - 1, pivotPosition);
            
            return pivotPosition;
        }
    }
    
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
    
    public static ClosestPairResult closestPairInSubset(ArrayList<Point2D> points, int start, int end) {
        Point2D point1 = null;
        Point2D point2 = null;
        
        int iterations = 0;
        long startTime = System.nanoTime();
        double minimunDistance = Double.MAX_VALUE;
        
        for (int i = start; i < end - 1; i++) {
            Point2D first = points.get(i);

            for (int j = i + 1; j < end; j++) {
                iterations++;
                Point2D second = points.get(j);
                double distance = first.distanceTo(second);

                if (distance < minimunDistance) {
                    point1 = first;
                    point2 = second;
                    minimunDistance = distance;
                }
            }
        }
        
        return new ClosestPairResult(point1, point2, minimunDistance, iterations, System.nanoTime() - startTime);
    }
    
    public static ClosestPairResult closestPair(ArrayList<Point2D> points) {
        Point2D point1 = null;
        Point2D point2 = null;
        
        int iterations = 0;
        long time = 0;
        double minimunDistance = Double.MAX_VALUE;
        int middle = points.size() / 2;
        
        int[][] groups = {
            {0, middle},
            {middle, points.size()}
        };
        
        double maxX1 = points.get(middle - 1).getX();
        double minX2 = points.get(middle).getX();
        
        for (int[] group : groups) {
            ClosestPairResult subsetResult = closestPairInSubset(points, group[0], group[1]);
            iterations += subsetResult.getIterations();
            time += subsetResult.getTime();
            
            if (subsetResult.getDistance() < minimunDistance) {
                minimunDistance = subsetResult.getDistance();
                point1 = subsetResult.getPoint1();
                point2 = subsetResult.getPoint2();
            }
        }
        
        ArrayList group3 = new ArrayList<>();
        
        for (int i = groups[0][1] - 1; minX2 - points.get(i).getX() < minimunDistance; i--)
            group3.add(points.get(i));
        
        for (int i = groups[1][0]; points.get(i).getX() - maxX1 < minimunDistance; i++)
            group3.add(points.get(i));
        
        if (group3.size() > 1) {
            ClosestPairResult group3Result = closestPairInSubset(group3, 0, group3.size());
            iterations += group3Result.getIterations();
            time += group3Result.getTime();

            if (group3Result.getDistance() < minimunDistance) {
                minimunDistance = group3Result.getDistance();
                point1 = group3Result.getPoint1();
                point2 = group3Result.getPoint2();
            }
        }
        
        return new ClosestPairResult(point1, point2, minimunDistance, iterations, time);
    }
    
    public static class InputFileHandler {
        public static void create(String filename) {
            File file = new File(filename);
            
            try {
                if (!file.exists())
                    file.delete();
            
                file.createNewFile();
                
                try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
                    for (int i = 0; i < 10000; i++) {
                        Point2D point = Point2D.randomPoint(100000, 100000);
                        out.write(point.getX() + "," + point.getY() + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public static ArrayList<Point2D> read(String filename) {
            ArrayList<Point2D> points = new ArrayList<>();
            File file = new File(filename);
            
            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                String line = in.readLine();
                
                while (line != null) {
                    String[] input = line.split(",");
                    Point2D point = new Point2D(Double.parseDouble(input[0]), Double.parseDouble(input[1]));
                    points.add(point);
                    line = in.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return points;
        }
    }
    
    public static double truncate2(double number) {
        return Double.parseDouble(new DecimalFormat("#.##").format(number));
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "data.txt";
        InputFileHandler.create(filename);
        ArrayList<Point2D> points = InputFileHandler.read(filename);
        Point2D.sort(points);
        ClosestPairResult result = closestPair(points);
        
        System.out.println("The closest points are " + result.getPoint1() + " and " + result.getPoint2() + ".\n" + "Distance: " + result.getPoint1().distanceTo(result.getPoint2()));
    }
    
}
