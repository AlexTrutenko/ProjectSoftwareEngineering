package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.ArrayList;
import java.util.List;

public class BloodPressureAlert implements AlertTriggerCondition{
    private final boolean systolic;     //we made it final boolean so If it is true the bp is systolic otherwise diastolic

    public BloodPressureAlert(boolean systolic) {
        this.systolic = systolic;
    }

    @Override
    public List<Alert> evaluate(int patientId, List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();
        List<PatientRecord> typeList = new ArrayList<>();
        String type;
        if (systolic) {
            type = "SystolicPressure";
        } else {
            type = "DiastolicPressure";
        }
        for (PatientRecord r : records) {
            if (type.equals(r.getRecordType())) {
                typeList.add(r);
            }
        }

        if(typeList.size() == 0) return alerts;

        PatientRecord latestRecord = typeList.get(typeList.size() - 1);
        double latestR = latestRecord.getMeasurementValue();
        long timeStamp = latestRecord.getTimestamp();
        if (systolic) {
            if (latestR > 180 || latestR < 90) {
                String cond = "Critical threshold breached for Systolic: " + latestR + " mmHg";
                alerts.add(new Alert(String.valueOf(patientId), cond, timeStamp));
            }
        } else {
            if (latestR > 120 || latestR < 60) {
                String cond = "Critical threshold breached for Diastolic: " + latestR + " mmHg";
                alerts.add(new Alert(String.valueOf(patientId), cond, timeStamp));
            }
        }
        if(typeList.size() >= 3) {
            for (int i = 2; i < typeList.size(); i++) {
                double v1 = typeList.get(i - 2).getMeasurementValue();
                double v2 = typeList.get(i - 1).getMeasurementValue();
                double v3 = typeList.get(i).getMeasurementValue();
                if (v2 - v1 > 10 && v3 - v2 > 10) {
                    String condition = type + " increasing trend (" + v1 + "->" + v2 + "->" + v3 + ")";
                    alerts.add(new Alert(String.valueOf(patientId), condition, typeList.get(i).getTimestamp()));
                } else if (v1 - v2 > 10 && v2 - v3 > 10) {
                    String condition = type + " decreasing trend (" + v1 + "->" + v2 + "->" + v3 + ")";
                    alerts.add(new Alert(String.valueOf(patientId), condition, typeList.get(i).getTimestamp()));
                }
            }
        }

        return alerts;
    }
}

