package com.alerts.Factory_Pattern;

import com.alerts.Alert;
/**
 * Factory for creating blood oxygen alerts.
 */

public class BloodOxygenAlertFactory extends AlertFactory{
    /**
     * Creates a blood oxygen alert with a condition-specific message.
     *
     * @param patientId  the ID of the patient
     * @param condition  the alert condition
     * @param timestamp  the time the alert was generated (in milliseconds)
     * @return a Blood Oxygen Alert instance
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp){
        return new Alert(patientId,"Blood Oxygen Alert: " + condition, timestamp);
    }
}
