package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.ArrayList;
import java.util.List;

/**
 * Monitors either systolic or diastolic blood pressure for critical threshold
 * breaches and sustained trends.
 *
 * If {@code systolic} is {@code true}, this alert checks records of type
 * {@code "SystolicPressure"} against critical thresholds (;90 mmHg or 180 mmHg);
 * otherwise it checks {@code "DiastolicPressure"} against thresholds (60 mmHg or 120 mmHg).
 * Additionally, it detects an increasing or decreasing trend when three consecutive
 * readings change by more than 10 mmHg in the same direction.
 *
 */

public class BloodPressureAlert implements AlertTriggerCondition{
    private final boolean systolic;     //we made it final boolean so If it is true the bp is systolic otherwise diastolic

    public BloodPressureAlert(boolean systolic) {
        this.systolic = systolic;
    }

    /**
     * Evaluates a list of {@link PatientRecord}s to generate blood pressure alerts.
     *
     * Filters the records for the configured pressure type ("SystolicPressure" or
     * "DiastolicPressure") and:
     *
     *  Checks the most recent reading against critical thresholds
     *  (systolic: &lt;90 or &gt;180; diastolic: &lt;60 or &gt;120 mmHg)
     *  and generates an alert if breached.
     *  When at least three readings are available, detects an increasing
     *  or decreasing trend of more than 10 mmHg between each consecutive reading
     *  and generates an alert for the third reading.
     *
     * @param patientId the unique identifier of the patient being evaluated
     * @param records   the complete list of {@link PatientRecord}s for this patient;
     *                  non-matching pressure records are ignored
     * @return a {@link List} of {@link Alert}s corresponding to any critical breaches
     *         or detected trends; an empty list if no alerts are triggered
     */

    @Override
    public List<Alert> evaluate(int patientId, List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();
        List<PatientRecord> typeList = new ArrayList<>();
        String type;
        if (systolic) {
            type = "SystolicPressure";
        }
        else {
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

