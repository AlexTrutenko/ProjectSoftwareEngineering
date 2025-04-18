package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.List;
import java.util.ArrayList;

public class ECGDataAlert implements AlertTriggerCondition {

    private static final int SLIDE_WINDOW_SIZE = 5;
    private static final double THRESHOLD_FACTOR = 1.5;

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
