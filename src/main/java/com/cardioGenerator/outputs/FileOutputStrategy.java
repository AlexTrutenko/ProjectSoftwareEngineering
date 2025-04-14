package com.cardioGenerator.outputs; //no underscores in package names


import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;
/** This class writes a patient data to different text files for each data category.
 * Each category is stored in its own file with patient ID, timestamp and value. 
 * Files are created under specific base directory.
 * 
 * @author Oleksandr Trutenko
 */

public class FileOutputStrategy implements OutputStrategy {         //class name should be UpperCamelCase

    private String baseDirectory;   //variable must be lowerCamelCase

    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();   //it should be lowerCamelCase
     /** Creates a file output strategy for the given directory. 
     * 
     * @param baseDirectory - the directory where the output files will be saved.
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory; //no space between
    }
    /** Adds patient data to a file associated with the data label.
     *  Each line is appended in a specific format: 
     * patient ID, timestamp, label, data.
     * 
     * @param patientID - a unique identification of a patient.
     * @param timestamp - the timestamp of data.
     * @param label - the type of data to output.
     * @param data - the actual data value.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        //lowerCamelCase
        //4.5.1 The primary goal for line wrapping is to have clear code, not necessarily code that fits in the smallest number of lines.
        String filePath = fileMap.computeIfAbsent(label, k ->
                Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (
                PrintWriter out = new PrintWriter(
                        Files.newBufferedWriter(Paths.get(filePath),
                                StandardOpenOption.CREATE,
                                StandardOpenOption.APPEND)))
        {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                    patientId, timestamp, label, data);      //4.5.1 where to break to avoid much text on a line
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
