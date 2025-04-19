package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.ArrayList;
import java.util.List;

public class BloodSaturationAlert implements AlertTriggerCondition {
    private static final long MS = 10 * 60 * 1000;  //timestap is measured in ms so it is converted to ms


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
            if (curr.getTimestamp() - prev.getTimestamp() <= MS && prev.getMeasurementValue() - curr.getMeasurementValue() >= 5) {
                String cond = "Rapid Oxygen Saturation Drop: "
                        + prev.getMeasurementValue() + "% to "
                        + curr.getMeasurementValue();
                alerts.add(new Alert(String.valueOf(patientId), cond, curr.getTimestamp()));

            }
        }

        return alerts;
    }
}
