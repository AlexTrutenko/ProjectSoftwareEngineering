package com.alerts.Factory_Pattern;

import com.alerts.Alert;
/**
 * Factory Factory for creating blood pressure alerts.
 */

public class ECGAlertFactory extends AlertFactory{
    /**
     * Creates an ECG alert with a condition-specific message.
     * 
     * @param patientId  the ID of the patient
     * @param condition  the alert condition
     * @param timestamp  the time the alert was generated (in milliseconds)
     * @return a Blood Pressure Alert instance
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp){
        return new Alert(patientId,"ECG Alert: " + condition, timestamp);
    }
}
