package com.cardioGenerator.generators;

import java.util.Random;

import com.cardioGenerator.outputs.OutputStrategy;
 /**This class generates simulated blood saturation for patients in a health monitoring system where each patient is initialized with a slightly randomized healthy baseline.
 * The generator maintains the last known saturation value for each patient and introduces small variations on each call to simulate real-time monitoring.
 * 
 * @author Oleksandr Trutenko
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;
    /** Constructs a new blood saturation data generator.
     *  Initialize each patient a baseline from 95 to 100 %.
     * @param patientCount - the total amount of patients.
    */

    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }
    /**Generates a blood saturation value for specific patient and outputs it using a giving output strategy.
     * Saturation level ranges over time and are clamped between 90% and 100%.
     * 
     * 
     * @param patientID - represents unique identification of a patient.
     * @param outputStrategy - The output strategy to which the data will be sent.
     * @return a blood saturation value.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
