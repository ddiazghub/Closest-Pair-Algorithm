/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package closestpair.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles output writing operations
*/
public class OutputFileHandler {
    private String filename;

    /**
     * Creates the output file
    */
    public OutputFileHandler(String filename) {
        this.filename = filename;
        File file = new File(filename);

        try {
            // Creates file
            if (file.exists())
                file.delete();

            file.createNewFile();

            try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
                out.write("size");

                for (int i = 0; i < 10; i++) {
                    out.write(" time" + i + " iterations" + i);
                }

                out.write(" avg_time avg_iterations\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes data to the output file
    */
    public void write(String data) {
        File file = new File(this.filename);

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}