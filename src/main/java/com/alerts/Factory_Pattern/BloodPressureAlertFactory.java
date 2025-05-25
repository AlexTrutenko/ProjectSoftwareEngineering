package com.alerts.Factory_Pattern;

import com.alerts.*;
/**
 * Factory for creating blood pressure alerts.
 */
public class BloodPressureAlertFactory extends AlertFactory{
    /**
     * Creates a blood pressure alert with a condition-specific message.
     * 
     * @param patientId  the ID of the patient
     * @param condition  the alert condition
     * @param timestamp  the time the alert was generated (in milliseconds)
     * @return a Blood Pressure Alert instance
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp){
        return new Alert(patientId,"Blood Pressure alert: "+ condition, timestamp);
    }
}
