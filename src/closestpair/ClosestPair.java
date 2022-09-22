/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package closestpair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author dedemoya
 */
public class ClosestPair {
    public static class Point2D {
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
        
        @Override
        public String toString() {
            return "(" + this.x + "," + this.y + ")";
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
        
        double midX = points.get(middle - 1).getX() + (points.get(middle).getX() - points.get(middle - 1).getX());
        
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
        
        for (int[] group : groups) {
            for (int i = group[0] - 1; Math.abs(points.get(i).getX() - middle) < minimunDistance / 2.0; i--)
                group3.add(points.get(i));
        }
        
        ClosestPairResult group3Result = closestPairInSubset(group3, 0, group3.size());
        iterations += group3Result.getIterations();
        time += group3Result.getTime();

        if (group3Result.getDistance() < minimunDistance) {
            minimunDistance = group3Result.getDistance();
            point1 = group3Result.getPoint1();
            point2 = group3Result.getPoint2();
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
                    String[] input = line.split(" ");
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "data.txt";
        ArrayList<Point2D> points = InputFileHandler.read(filename);
        ClosestPairResult result = closestPair(points);
        
        System.out.println("The closest points are " + result.getPoint1() + " and " + result.getPoint2() + ".\n" + "Distance: " + result.getPoint1().distanceTo(result.getPoint2()));
    }
    
}
