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
        private final int iterations;
        private final long time;

        public ClosestPairResult(Point2D point1, Point2D point2, int iterations, long time) {
            this.point1 = point1;
            this.point2 = point2;
            this.iterations = iterations;
            this.time = time;
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
    }
    
    public static ClosestPairResult closestPair(ArrayList<Point2D> points) {
        Point2D point1 = null;
        Point2D point2 = null;
        int iterations = 0;
        long startTime = System.nanoTime();
        double minimunDistance = Double.MAX_VALUE;
        
        for (int i = 0; i < points.size() - 1; i++) {
            Point2D first = points.get(i);
            
            for (int j = 0; j < points.size(); j++) {
                iterations++;
                
                if (i == j)
                    continue;
                
                Point2D second = points.get(j);
                double distance = first.distanceTo(second);
                
                if (distance < minimunDistance) {
                    point1 = first;
                    point2 = second;
                    minimunDistance = distance;
                }
            }
        }
        
        return new ClosestPairResult(point1, point2, iterations, System.nanoTime() - startTime);
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
