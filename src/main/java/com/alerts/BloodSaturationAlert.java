package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.ArrayList;
import java.util.List;

/**
 * Monitors a patient’s oxygen saturation measurements and generates alerts
 * for both low saturation levels and rapid drops over a short interval.
 *
 * Two types of alerts are produced:
 *
 *   Low Saturation Alert: Any individual reading below 92%.
 *   Rapid Drop Alert: A decrease of ≥5 percentage points
 *       within a 10-minute interval.
 * The time window for detecting rapid drops is defined by {@link #MS},
 * which converts 10 minutes into milliseconds (since timestamps are in ms).
 *
 */

public class BloodSaturationAlert implements AlertTriggerCondition {
    private static final long MS = 10 * 60 * 1000;  //timestap is measured in ms so it is converted to ms

    /**
     * Evaluates a list of {@link PatientRecord}s to detect oxygen saturation issues.
     *
     * First, collects all records of type "OxygenSaturation" and generates an alert
     * for any measurement below 92%. Then, scans consecutive saturation readings:
     * if two readings occur within {@value #MS} ms and drop by 5% or more,
     * a rapid‐drop alert is generated.
     *
     * @param patientId the unique identifier of the patient whose records are being evaluated
     * @param records   the complete list of {@link PatientRecord}s for this patient;
     *                  non-saturation records are ignored
     * @return a {@link List} of {@link Alert}s for detected low saturation or rapid drop events;
     *         returns an empty list if no conditions are met
     */

    @Override
    public List<Alert> evaluate(int patientId, List<PatientRecord> records) {

        //Low Saturation Alert:
        List<Alert> alerts = new ArrayList<>();
        List<PatientRecord> satRecords = new ArrayList<>();
        for (PatientRecord r : records) {
            if ("OxygenSaturation".equals(r.getRecordType())) {
                satRecords.add(r);
                if (r.getMeasurementValue() < 92) {
                    String cond = "Low Oxygen Saturation: " + r.getMeasurementValue() + "%";
                    alerts.add(new Alert(String.valueOf(patientId), cond, r.getTimestamp()));

                }
            }
        }

        //Rapid Drop Alert:
        for (int i = 1; i < satRecords.size(); i++) {
            PatientRecord prev = satRecords.get(i - 1);
            PatientRecord curr = satRecords.get(i);
            if (curr.getTimestamp() - prev.getTimestamp() <= MS &&
                    prev.getMeasurementValue() - curr.getMeasurementValue() >= 5) {
                String cond = "Rapid Oxygen Saturation Drop: "
                        + prev.getMeasurementValue() + "% to "
                        + curr.getMeasurementValue();
                alerts.add(new Alert(String.valueOf(patientId), cond, curr.getTimestamp()));

            }
        }

        return alerts;
    }
}
