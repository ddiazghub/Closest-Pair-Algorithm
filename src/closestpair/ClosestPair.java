/*
 * Algorithms and Complexity                                August 5, 2022
 * IST 4310
 * Prof. M. Diaz-Maldonado
 * Name: David Eduardo Díaz de Moya
 *
 * Synopsis:
 * Finds the closest pair in a group of points.
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

import closestpair.result.ClosestPairResult;
import closestpair.result.SpacePartitionResult;
import closestpair.io.OutputFileHandler;
import closestpair.io.InputFileHandler;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author dedemoya
 */
public class ClosestPair {
    /**
     * Recursively finds the closest pair of points in a list of points
    */
    public static ClosestPairResult closestPair(ArrayList<Point2D> points) {
        Point2D point1 = null;
        Point2D point2 = null;
        
        // Splits the list of points into space partitions and starts counting iterations, and elapsed time
        SpacePartitionResult partitionResult = SpacePartition2D.partition(points);
        long iterations = partitionResult.getIterations();
        long time = partitionResult.getTime();
        long minimunDistance = Long.MAX_VALUE;
        ArrayList<SpacePartition2D> partitions = partitionResult.getPartitions();
        
        // For each partition
        for (SpacePartition2D partition : partitions) {
            // Finds the closest pair in the partition
            ClosestPairResult result = partition.closestPair();
            iterations += 1 + result.getIterations();
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
                iterations += 1 + result.getIterations();
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "data.txt";

        // Creates the output directory if it does not exist
        File outdir = new File("output");
        
        if (!outdir.exists())
            outdir.mkdir();
        
        // Creates the output files
        OutputFileHandler recursiveOutput = new OutputFileHandler("output/recursive.txt");
        OutputFileHandler bruteForceOutput = new OutputFileHandler("output/brute_force.txt");
        
        // Increases the input size by 2 up until it becomes greater than 50000
        int iMax = 50000;
        
        for (int i = 2; i < iMax; i *= 2) {
            // Average values for all repetitions
            double recAvgI = 0;
            double recAvgT = 0;
            double bfAvgI = 0;
            double bfAvgT = 0;
            
            recursiveOutput.write(i + "");
            bruteForceOutput.write(i + "");
            
            // Repeats 10 times for each input size
            for (int j = 0; j < 10; j++) {
                // Creates points and sorts
                InputFileHandler.create(filename, i);
                ArrayList<Point2D> points = InputFileHandler.read(filename);
                Point2D.sort(points);
                
                // Runs recursive algorithm
                System.out.println("Running recursive algorithm for " + i + " points " + j + " out of 10 times");
                ClosestPairResult recursiveResult = closestPair(points);
                recursiveOutput.write(" " + recursiveResult.getTime() + " " + recursiveResult.getIterations());
                recAvgI += (double) recursiveResult.getIterations() / 10;
                recAvgT += (double) recursiveResult.getTime() / 10;
                
                // Runs brute force algorithm
                System.out.println("Running brute force algorithm for " + i + " points " + j + " out of 10 times");
                ClosestPairResult bruteForceResult = new SpacePartition2D(points).closestPair();
                bruteForceOutput.write(" " + bruteForceResult.getTime() + " " + bruteForceResult.getIterations());
                bfAvgI += (double) bruteForceResult.getIterations() / 10;
                bfAvgT += (double) bruteForceResult.getTime() / 10;
            }
            
            // Writes average values to output file
            recursiveOutput.write(" " + recAvgT + " " + recAvgI + "\n");
            bruteForceOutput.write(" " + bfAvgT + " " + bfAvgI + "\n");
        }
    }
}
