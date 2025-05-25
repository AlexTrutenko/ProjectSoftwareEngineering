package com.alerts.Strategy_Pattern;

import java.util.ArrayList;
import java.util.List;

import com.alerts.Alert;
import com.dataManagement.PatientRecord;
/**
 * Strategy for generating alerts based on heart rate readings.
 */
public class HeartRateStrategy implements AlertStrategy{
    private final double minBound = 50;
    private final double maxBound = 120;
    /**
     * Checks patient records for abnormal heart rate values and creates alerts.
     *
     * @param patientId the ID of the patient
     * @param records   the list of patient records to check
     * @return a list of alerts generated for abnormal heart rates
     */
    @Override
    public List<Alert> checkAlert(String patientId, List<PatientRecord> records){
        List<Alert> alerts = new ArrayList<>();

        for(PatientRecord r : records){
            if ("HeartRate".equals(r.getRecordType())) {
                double value = r.getMeasurementValue();

                if (value < minBound) {
                    alerts.add(new Alert(patientId, "Low heart rate: " + value, r.getTimestamp()));
                } else if (value > maxBound) {
                    alerts.add(new Alert(patientId, "High heart rate: " + value, r.getTimestamp()));
                }
            } 
        }
        return alerts;
    }
}
