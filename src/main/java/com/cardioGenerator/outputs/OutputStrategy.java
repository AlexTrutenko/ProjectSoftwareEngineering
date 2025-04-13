package com.cardioGenerator.outputs;
/**Defines an interface for output of generated patient health data.
 * This interface defines how the data will be outputted. For instance, printing in console, creating a file, etc.
 * 
 * @author Oleksandr Trutenko
 */
public interface OutputStrategy {
    /** Outputs a health data report for a specific patient.
     * @param patientId - identificator of a patient.
     * @param timestamp - The time the data was generated.
     * @param label - A label to describe a type of data.
     * @param data - The actual data as a string.
     */
    void output(int patientId, long timestamp, String label, String data);
}
