package com.alerts;

import com.dataManagement.PatientRecord;
import java.util.List;

public interface AlertTriggerCondition {
    List<Alert> evaluate(int patientId, List<PatientRecord> records);
}
