package com.cardioGenerator.generators;

import com.cardioGenerator.outputs.OutputStrategy;
     /** An interface for generating simulated health data for patient.
     * The classes that are implementing this interface are responsible for producing a specific type of patient data such as ECG, blood pressure, blood saturation, or other vital signs. 
     * This interface provides a unified contract for invoking data generation regardless of the type.
     * 
     * @author Oleksandr Trutenko
     */
public interface PatientDataGenerator {
     /**Generates health data for a specific patient and sends it to the provided output strategy.
     * 
     * @param patientId - a unique identification of a patient.
     * @param outputStrategy - The output strategy to which the data will be sent.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
