package com.cardioGenerator.generators;

import java.util.Random;

import com.cardioGenerator.outputs.OutputStrategy;
    /**This class represents an alert generator for patient monitoring system and implements PatientDataGenerator interface.
    This class is responsible for simulating alert events based on probabilistic models.
    * This code uses a Poisson process to model realistic medical event frequencies.
     * 
     * @author Oleksandr Trutenko
     */

public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    private boolean[] AlertStates; // false = resolved, true = pressed

    /** Craetes an array of boolean values that contains the alert events. Sets the size for the array by what is given in patientCount.
     * @param patientCount - represents amount of patients in the medical system.
     */
    public AlertGenerator(int patientCount) {
        AlertStates = new boolean[patientCount + 1];
    }

    /** Generates an alert state for specific patient relying on current state and random chance.
       if patient's alert status is true, it has a 90% chance of being resolved
       if patient's alert status is false, the new alert may be triggered using a Poisson-based model.

       @return The method output is either "resolved" or "triggered"
       @param patientID - represents patient ID
       @param outputStrategy - the strategy used to handle the generated alert data
     * 
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (AlertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    AlertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double Lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-Lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    AlertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
