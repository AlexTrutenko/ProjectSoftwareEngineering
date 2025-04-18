package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.List;
import java.util.ArrayList;

public class HypotensiveHypoxemiaAlert implements AlertTriggerCondition {
    @Override
    public List<Alert> evaluate(int patientId, List<PatientRecord> records) {
        Double SystolicPressure = 0.0;
        Double Saturation = 0.0;
        long timeStamp = 0;
        for (PatientRecord r : records) {
            if ("SystolicPressure".equals(r.getRecordType())) {
                SystolicPressure = r.getMeasurementValue();
                timeStamp = r.getTimestamp();
            }
            if ("OxygenSaturation".equals(r.getRecordType())) {
                Saturation = r.getMeasurementValue();
                timeStamp = r.getTimestamp();
            }
        }
        List<Alert> alerts = new ArrayList<>();
        if (SystolicPressure < 90 && Saturation < 92) {
            String cond = "Hypotensive Hypoxemia: systolic=" + SystolicPressure
                    + " mmHg, saturation=" + Saturation + "%";
            alerts.add(new Alert(String.valueOf(patientId), cond, timeStamp));
        }
        return alerts;
    }
}
