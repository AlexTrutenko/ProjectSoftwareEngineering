package com.alerts.Strategy_Pattern;

import java.util.ArrayList;
import java.util.List;

import com.alerts.Alert;
import com.dataManagement.PatientRecord;
/**
 * Strategy for generating alerts based on blood pressure readings.
 * Detects abnormal systolic and diastolic pressure values and creates alerts accordingly.
 */
public class BloodPressureStrategy implements AlertStrategy{
    private final double systolicMaxBoundary = 140;
    private final double systolicMinBoundary = 90;
    private final double diastolicMaxBoundary = 90;
    private final double diastolicMinBoundary = 60;
    /**
     * Checks a list of patient records for abnormal blood pressure values.
     *
     * @param patientId the ID of the patient
     * @param records list of patient records to evaluate
     * @return list of generated alerts based on blood pressure thresholds
     */
    @Override
    public List<Alert> checkAlert(String patientId, List<PatientRecord> records){
        List<Alert> alerts = new ArrayList<>();

        for(PatientRecord r : records){
            String systolicOrDiastolic = r.getRecordType();
            double pressureVal = r.getMeasurementValue();
            if("BloodPressureSystolic".equals(systolicOrDiastolic)){
                if(pressureVal < systolicMinBoundary){
                    alerts.add(new Alert(patientId, "Low systolic pressure: " + pressureVal, r.getTimestamp()));
                }
                if(pressureVal > systolicMaxBoundary){
                    alerts.add(new Alert(patientId, "High systolic pressure: " + pressureVal, r.getTimestamp()));
                }
            }
            if ("BloodPressureDiastolic".equals(systolicOrDiastolic)){
                if(pressureVal < diastolicMinBoundary){
                    alerts.add(new Alert(patientId, "Low diastolic pressure: " + pressureVal, r.getTimestamp()));
                }
                if(pressureVal > diastolicMaxBoundary){
                    alerts.add(new Alert(patientId, "High diastolic pressure: " + pressureVal, r.getTimestamp()));
                }
            }

        }
        return alerts;

    }
}
