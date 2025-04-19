package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.List;
import java.util.ArrayList;

public class HypotensiveHypoxemiaAlert implements AlertTriggerCondition {
    @Override
    public List<Alert> evaluate(int patientId, List<PatientRecord> records) {
        double systolicPressure = 0.0;
        double saturation = 0.0;
        long timeStamp = 0;
        for (PatientRecord r : records) {
            if ("SystolicPressure".equals(r.getRecordType())) {
                systolicPressure = r.getMeasurementValue();
                timeStamp = r.getTimestamp();
            }
            if ("OxygenSaturation".equals(r.getRecordType())) {
                saturation = r.getMeasurementValue();
                timeStamp = r.getTimestamp();
            }
        }
        List<Alert> alerts = new ArrayList<>();
        if (systolicPressure < 90 && saturation < 92) {
            String cond = "Hypotensive Hypoxemia: systolic=" + systolicPressure
                    + " mmHg, saturation=" + saturation + "%";
            alerts.add(new Alert(String.valueOf(patientId), cond, timeStamp));
        }
        return alerts;
    }
}
