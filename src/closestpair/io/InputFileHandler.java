/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package closestpair.io;

import closestpair.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class containing methods for handling input data
*/
public class InputFileHandler {
    /**
     * Creates a file containing random points
    */
    public static void create(String filename, int n) {
        File file = new File(filename);

        try {
            // Creates file
            if (file.exists())
                file.delete();

            file.createNewFile();

            try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
                // Writes n random unique points to the file
                HashSet<Point2D> set = new HashSet<>();

                while (set.size() < n) {
                    Point2D point = Point2D.randomPoint(10 * n, 20);

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
     * Reads a file containing points and sequentially writes those points to an arraylist
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