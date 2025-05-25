package com.alerts.Strategy_Pattern;

import java.util.List;

import com.alerts.Alert;
import com.dataManagement.PatientRecord;
/**
 * Interface for strategies.
 * @param patientID the ID of the patient
 * @param records list of patient records to evaluate
 */

public interface AlertStrategy {
    List<Alert> checkAlert(String patientId, List<PatientRecord> records);
}
