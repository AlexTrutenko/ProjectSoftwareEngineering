package com.alerts.Strategy_Pattern;

import java.util.ArrayList;
import java.util.List;

import com.alerts.Alert;
import com.dataManagement.PatientRecord;

/**
 * Strategy for generating alerts based on oxygen saturation readings.
 */
public class OxygenSaturationStrategy implements AlertStrategy{
    private final double saturationLevelBoundary = 90; //In percentages
    /**
     * Checks patient records for oxygen saturation below the threshold.
     *
     * @param patientId the ID of the patient
     * @param records  the list of patient records to check
     * @return a list of alerts generated for low oxygen saturation
     */
    @Override
    public List<Alert> checkAlert(String patientId, List<PatientRecord> records){
        List<Alert> alerts = new ArrayList<>();
        for(PatientRecord r : records){
            if("OxygenSaturation".equals(r.getRecordType())){
                double oxygenLevel = r.getMeasurementValue();
                if(oxygenLevel < saturationLevelBoundary){
                    alerts.add(new Alert(patientID, "Oxygen Saturation. Oxygen level: " + oxygenLevel, r.getTimestamp()));
                }
            }
        }
        return alerts;
    }
}
