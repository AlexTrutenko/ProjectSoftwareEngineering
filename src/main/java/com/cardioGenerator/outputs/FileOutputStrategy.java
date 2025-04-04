package com.cardioGenerator.outputs; //no underscores in package names


import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy {         //class name should be UpperCamelCase

    private String baseDirectory;   //variable must be lowerCamelCase

    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();   //it should be lowerCamelCase

    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory; //no space between
    }

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