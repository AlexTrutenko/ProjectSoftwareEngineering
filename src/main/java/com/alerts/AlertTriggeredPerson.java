package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.ArrayList;
import java.util.List;

/**
 * Monitors patient “AlertButton” events and generates an alert whenever the
 * button is pressed or released.
 *
 * Each {@link PatientRecord} with {@code getRecordType() == "AlertButton"} is
 * inspected. A {@code measurementValue} of 1.0 signifies a button press,
 * while any other value signifies a release. An {@link Alert} is generated
 * for each event with the corresponding timestamp.
 */

public class AlertTriggeredPerson implements AlertTriggerCondition {

    /**
     * Evaluates a list of {@link PatientRecord}s for alert-button actions.
     *
     * Iterates through all provided records and, for each record of type
     * {@code "AlertButton"}, interprets the measurement value:
     *
     *  {@code 1.0}: button pressed
     *  otherwise: button released
     *
     * An {@link Alert} is created for each event with the appropriate action
     * description and timestamp.
     *
     * @param patientId the unique identifier of the patient whose records are being evaluated
     * @param records   the complete list of {@link PatientRecord}s for this patient;
     *                  only records with {@code getRecordType() == "AlertButton"} are considered
     * @return a {@link List} of {@link Alert}s for each press or release event;
     *         returns an empty list if no alert-button records are found
     */

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
