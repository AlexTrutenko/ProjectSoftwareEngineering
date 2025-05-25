package com.alerts.Factory_Pattern;
import com.alerts.*;
/**
 * Abstract factory for creating Alert objects.
 */

public abstract class AlertFactory {
    /**
     * Creates an Alert instance with the given parameters.
     *
     * @param patientId  the ID of the patient
     * @param condition  the alert condition
     * @param timestamp  the time the alert was generated (in milliseconds)
     * @return a new Alert instance
     */
    public abstract Alert createAlert(String patientId, String condition, long timestamp);
}
