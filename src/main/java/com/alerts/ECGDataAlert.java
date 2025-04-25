package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.List;
import java.util.ArrayList;

/**
 * Checks ECG data for abnormal spikes using a sliding-window approach.
 *
 * This implementation of {@link AlertTriggerCondition} filters only ECG records
 * from a patient’s list of {@link PatientRecord}s, then computes a moving average
 * over a fixed window. If the most recent ECG measurement in the window exceeds
 * the average by a configurable threshold factor, an alert is generated.
 *
 *
 *  Window size: {@value #SLIDE_WINDOW_SIZE} records
 *  Threshold factor: {@value #THRESHOLD_FACTOR}× the window average
 */
public class ECGDataAlert implements AlertTriggerCondition {

    private static final int SLIDE_WINDOW_SIZE = 5;
    private static final double THRESHOLD_FACTOR = 1.5;

    /**
     * Evaluates a list of patient records for ECG anomalies.
     *
     * Only records whose {@code getRecordType()} equals "ECG" are considered.
     * Once at least {@value #SLIDE_WINDOW_SIZE} ECG measurements are collected,
     * a sliding window average is computed. If the latest measurement in the window
     * exceeds the sliding average multiplied by {@value #THRESHOLD_FACTOR}, an
     * {@link Alert} is created for that timestamp and the evaluation stops.
     *
     * @param patientId the unique identifier of the patient whose records are being evaluated
     * @param records   the complete list of {@link PatientRecord}s for this patient;
     *                  non-ECG records are ignored internally
     * @return a {@link List} of {@link Alert}s for any detected abnormal ECG readings;
     *         returns an empty list if no alerts are triggered
     */

    @Override
    public List<Alert> evaluate(int patientId, List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();
        List<PatientRecord> ecgRecords = new ArrayList<>();
        for (PatientRecord r : records) {
            if ("ECG".equals(r.getRecordType())) {
                ecgRecords.add(r);
            }
        }

        if (ecgRecords.size() < SLIDE_WINDOW_SIZE) return alerts;

        for (int i = SLIDE_WINDOW_SIZE - 1; i < ecgRecords.size(); i++) {
            double sum = 0.0;
            for (int j = i - SLIDE_WINDOW_SIZE + 1; j <= i; j++) {
                sum += ecgRecords.get(j).getMeasurementValue();
            }
            double avg = sum / SLIDE_WINDOW_SIZE;
            PatientRecord curr = ecgRecords.get(i);
            double value = curr.getMeasurementValue();
            if (value > avg * THRESHOLD_FACTOR) {
                String cond = "Measured abnormally high ECG record!";
                alerts.add(new Alert(String.valueOf(patientId), cond, curr.getTimestamp()));
                break;
            }
        }
        return alerts;
    }
}
