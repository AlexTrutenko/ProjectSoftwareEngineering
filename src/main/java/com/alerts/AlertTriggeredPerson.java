package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.ArrayList;
import java.util.List;

public class AlertTriggeredPerson implements AlertTriggerCondition {
    @Override
    public List<Alert> evaluate(int patientId, List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();
        for (PatientRecord r : records) {
            if ("AlertButton".equals(r.getRecordType())) {
                String action;
                if (r.getMeasurementValue() == 1.0) {
                    action = "pressed";
                } else {
                    action = "released";
                }
                String cond = "Alert button " + action;
                alerts.add(new Alert(String.valueOf(patientId), cond, r.getTimestamp()));
            }
        }
        return alerts;
    }
}
