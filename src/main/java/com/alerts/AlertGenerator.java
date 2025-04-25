package com.alerts;

//no underscores, Package names use only lowercase letters and digits
import com.dataManagement.DataStorage;
import com.dataManagement.Patient;
import com.dataManagement.PatientRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private final DataStorage dataStorage;
    private final List<AlertTriggerCondition> triggerConditions;
    private final List<Alert> generatedAlerts;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     *
     * Initialize alert conditions:
     *     - Blood pressure (systolic & diastolic)
     *     - Blood saturation
     *     - Hypotensive hypoxemia
     *     - ECG anomalies
     *     - Manual alert‐button events
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.generatedAlerts = new ArrayList<>();
        this.triggerConditions = Arrays.asList(
                new BloodPressureAlert(true),          // Systolic
                new BloodPressureAlert(false),         // Diastolic
                new BloodSaturationAlert(),
                new HypotensiveHypoxemiaAlert(),
                new ECGDataAlert(),
                new AlertTriggeredPerson()
        );
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */

    public void evaluateData(Patient patient) {
        int patientId = patient.getPatientId();
        long now = System.currentTimeMillis();
        List<PatientRecord> records = dataStorage.getRecords(patientId, 0, now);

        for (AlertTriggerCondition triggerCondition : triggerConditions) {
            List<Alert> alerts = triggerCondition.evaluate(patientId, records);
            for (Alert alert : alerts) {
                triggerAlert(alert);
            }
        }
    }
    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("Attention! Patient " + alert.getPatientId() + ": "
                + alert.getCondition() + " at " + alert.getTimestamp());
        generatedAlerts.add(alert);
    }
    public List<Alert> getGeneratedAlerts() {
        return new ArrayList<>(generatedAlerts);
    }
}
